package com.knife.router4j.feign.client;

import com.knife.router4j.common.util.PathRuleUtil;
import com.knife.router4j.common.util.UrlUtil;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import static feign.Util.*;
import static java.lang.String.format;

/**
 * Router4j提供的基于Default的客户端
 *   对此类的进行了修改：{@link Client.Default}
 *   修改的方法是：{@link Router4jDefaultClient#execute(Request, Request.Options)}
 */
public class Router4jDefaultClient implements Client {
    private final SSLSocketFactory sslContextFactory;
    private final HostnameVerifier hostnameVerifier;

    @Autowired
    private UrlUtil urlUtil;

    /**
     * Disable the request body internal buffering for {@code HttpURLConnection}.
     *
     * @see HttpURLConnection#setFixedLengthStreamingMode(int)
     * @see HttpURLConnection#setFixedLengthStreamingMode(long)
     * @see HttpURLConnection#setChunkedStreamingMode(int)
     */
    private final boolean disableRequestBuffering;

    /**
     * Create a new client, which disable request buffering by default.
     *
     * @param sslContextFactory SSLSocketFactory for secure https URL connections.
     * @param hostnameVerifier  the host name verifier.
     */
    public Router4jDefaultClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier) {
        this.sslContextFactory = sslContextFactory;
        this.hostnameVerifier = hostnameVerifier;
        this.disableRequestBuffering = true;
    }

    /**
     * Create a new client.
     *
     * @param sslContextFactory       SSLSocketFactory for secure https URL connections.
     * @param hostnameVerifier        the host name verifier.
     * @param disableRequestBuffering Disable the request body internal buffering for
     *                                {@code HttpURLConnection}.
     */
    public Router4jDefaultClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier,
                                 boolean disableRequestBuffering) {
        super();
        this.sslContextFactory = sslContextFactory;
        this.hostnameVerifier = hostnameVerifier;
        this.disableRequestBuffering = disableRequestBuffering;
    }

    /**
     * 只修改了此方法
     */
    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        String newUrl = urlUtil.modifyWithRule(request.url()).toString();

        Request newRequest = Request.create(request.httpMethod(),
                newUrl,
                request.headers(),
                Request.Body.create(request.body()),
                request.requestTemplate());
        HttpURLConnection connection = convertAndSend(newRequest, options);
        return convertResponse(connection, newRequest);
    }

    Response convertResponse(HttpURLConnection connection, Request request) throws IOException {
        int status = connection.getResponseCode();
        String reason = connection.getResponseMessage();

        if (status < 0) {
            throw new IOException(format("Invalid status(%s) executing %s %s", status,
                    connection.getRequestMethod(), connection.getURL()));
        }

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
            // response message
            if (field.getKey() != null) {
                headers.put(field.getKey(), field.getValue());
            }
        }

        Integer length = connection.getContentLength();
        if (length == -1) {
            length = null;
        }
        InputStream stream;
        if (status >= 400) {
            stream = connection.getErrorStream();
        } else {
            stream = connection.getInputStream();
        }
        return Response.builder()
                .status(status)
                .reason(reason)
                .headers(headers)
                .request(request)
                .body(stream, length)
                .build();
    }

    public HttpURLConnection getConnection(final URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    HttpURLConnection convertAndSend(Request request, Request.Options options) throws IOException {
        final URL url = new URL(request.url());
        final HttpURLConnection connection = this.getConnection(url);
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection sslCon = (HttpsURLConnection) connection;
            if (sslContextFactory != null) {
                sslCon.setSSLSocketFactory(sslContextFactory);
            }
            if (hostnameVerifier != null) {
                sslCon.setHostnameVerifier(hostnameVerifier);
            }
        }
        connection.setConnectTimeout(options.connectTimeoutMillis());
        connection.setReadTimeout(options.readTimeoutMillis());
        connection.setAllowUserInteraction(false);
        connection.setInstanceFollowRedirects(options.isFollowRedirects());
        connection.setRequestMethod(request.httpMethod().name());

        Collection<String> contentEncodingValues = request.headers().get(CONTENT_ENCODING);
        boolean gzipEncodedRequest =
                contentEncodingValues != null && contentEncodingValues.contains(ENCODING_GZIP);
        boolean deflateEncodedRequest =
                contentEncodingValues != null && contentEncodingValues.contains(ENCODING_DEFLATE);

        boolean hasAcceptHeader = false;
        Integer contentLength = null;
        for (String field : request.headers().keySet()) {
            if (field.equalsIgnoreCase("Accept")) {
                hasAcceptHeader = true;
            }
            for (String value : request.headers().get(field)) {
                if (field.equals(CONTENT_LENGTH)) {
                    if (!gzipEncodedRequest && !deflateEncodedRequest) {
                        contentLength = Integer.valueOf(value);
                        connection.addRequestProperty(field, value);
                    }
                } else {
                    connection.addRequestProperty(field, value);
                }
            }
        }
        // Some servers choke on the default accept string.
        if (!hasAcceptHeader) {
            connection.addRequestProperty("Accept", "*/*");
        }

        if (request.body() != null) {
            if (disableRequestBuffering) {
                if (contentLength != null) {
                    connection.setFixedLengthStreamingMode(contentLength);
                } else {
                    connection.setChunkedStreamingMode(8196);
                }
            }
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            if (gzipEncodedRequest) {
                out = new GZIPOutputStream(out);
            } else if (deflateEncodedRequest) {
                out = new DeflaterOutputStream(out);
            }
            try {
                out.write(request.body());
            } finally {
                try {
                    out.close();
                } catch (IOException suppressed) { // NOPMD
                }
            }
        }
        return connection;
    }
}