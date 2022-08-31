package com.knife.router4j.feign.client;

import com.knife.router4j.common.util.UrlUtil;
import feign.Client;
import feign.Request.HttpMethod;
import feign.Request.Options;
import feign.Response;
import feign.Response.Body;
import feign.okhttp.OkHttpClient;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Router4j提供的基于OkHttpClient的客户端
 *   对此类的进行了修改：{@link OkHttpClient}
 *   修改的方法是：{@link Router4jOkHttpClient#execute(feign.Request, feign.Request.Options)}
 */
public final class Router4jOkHttpClient implements Client {
    private final okhttp3.OkHttpClient delegate;

    @Autowired
    private UrlUtil urlUtil;

    public Router4jOkHttpClient() {
        this(new okhttp3.OkHttpClient());
    }

    public Router4jOkHttpClient(okhttp3.OkHttpClient delegate) {
        this.delegate = delegate;
    }

    static Request toOkHttpRequest(feign.Request input) {
        Builder requestBuilder = new Builder();
        requestBuilder.url(input.url());
        MediaType mediaType = null;
        boolean hasAcceptHeader = false;
        Iterator var4 = input.headers().keySet().iterator();

        while(var4.hasNext()) {
            String field = (String)var4.next();
            if (field.equalsIgnoreCase("Accept")) {
                hasAcceptHeader = true;
            }

            Iterator var6 = ((Collection)input.headers().get(field)).iterator();

            while(var6.hasNext()) {
                String value = (String)var6.next();
                requestBuilder.addHeader(field, value);
                if (field.equalsIgnoreCase("Content-Type")) {
                    mediaType = MediaType.parse(value);
                    if (input.charset() != null) {
                        mediaType.charset(input.charset());
                    }
                }
            }
        }

        if (!hasAcceptHeader) {
            requestBuilder.addHeader("Accept", "*/*");
        }

        byte[] inputBody = input.body();
        boolean isMethodWithBody = HttpMethod.POST == input.httpMethod()
                || HttpMethod.PUT == input.httpMethod()
                || HttpMethod.PATCH == input.httpMethod();
        if (isMethodWithBody) {
            requestBuilder.removeHeader("Content-Type");
            if (inputBody == null) {
                inputBody = new byte[0];
            }
        }

        RequestBody body = inputBody != null
                ? RequestBody.create(mediaType, inputBody)
                : null;
        requestBuilder.method(input.httpMethod().name(), body);
        return requestBuilder.build();
    }

    private static Response toFeignResponse(okhttp3.Response response,
                                            feign.Request request) throws IOException {
        return Response.builder()
                .status(response.code())
                .reason(response.message())
                .request(request)
                .headers(toMap(response.headers()))
                .body(toBody(response.body()))
                .build();
    }

    private static Map<String, Collection<String>> toMap(Headers headers) {
        return (Map)headers.toMultimap();
    }

    private static Body toBody(final ResponseBody input) throws IOException {
        if (input != null && input.contentLength() != 0L) {
            final Integer length = input.contentLength() >= 0L
                    && input.contentLength() <= 2147483647L
                    ? (int)input.contentLength()
                    : null;
            return new Body() {
                public void close() throws IOException {
                    input.close();
                }

                public Integer length() {
                    return length;
                }

                public boolean isRepeatable() {
                    return false;
                }

                public InputStream asInputStream() throws IOException {
                    return input.byteStream();
                }

                public Reader asReader() throws IOException {
                    return input.charStream();
                }

                public Reader asReader(Charset charset) throws IOException {
                    return this.asReader();
                }
            };
        } else {
            if (input != null) {
                input.close();
            }

            return null;
        }
    }

    public Response execute(feign.Request input, Options options) throws IOException {
        okhttp3.OkHttpClient requestScoped;
        if (this.delegate.connectTimeoutMillis() == options.connectTimeoutMillis()
                && this.delegate.readTimeoutMillis() == options.readTimeoutMillis()
                && this.delegate.followRedirects() == options.isFollowRedirects()) {
            requestScoped = this.delegate;
        } else {
            requestScoped = this.delegate.newBuilder()
                    .connectTimeout((long)options.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .readTimeout((long)options.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .followRedirects(options.isFollowRedirects()).build();
        }

        String newUrl = urlUtil.modifyWithRule(input.url()).toString();
        feign.Request newRequest = feign.Request.create(input.httpMethod(), newUrl, input.headers(),
                feign.Request.Body.create(input.body()), input.requestTemplate());

        Request request = toOkHttpRequest(newRequest);
        okhttp3.Response response = requestScoped.newCall(request).execute();
        return toFeignResponse(response, newRequest).toBuilder().request(newRequest).build();
    }
}
