package com.knife.gateway.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Objects;


/**
 * 动态路由
 */
@Slf4j
@Component
public class Router4jFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest originalRequest = exchange.getRequest();

        // 可获得所有请求参数
        // Map<String, String> cachedRequestBody = exchange
        // .getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);

        //获取域名+端口后的path
        String rawPath = originalRequest.getURI().getRawPath();

        // todo 从redis中取出所有url，然后用rawPath去匹配

        String host = "localhost";
        int port = 9012;

        URI originUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);

        URI newUri = UriComponentsBuilder.fromUri(originUri)
                .host(host)
                .port(port)
                .build()
                .toUri();

        //重新封装request对象
        ServerHttpRequest newRequest = originalRequest.mutate().uri(newUri).build();

        // NettyRoutingFilter 最终从GATEWAY_REQUEST_URL_ATTR 取出uri对象进行http请求
        // 所以这里要将新的对象覆盖进去
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newUri);

        return chain.filter(exchange.mutate().request(newRequest).build());

        // 也可以加回调方法
        // return chain.filter(exchange.mutate().request(newRequest).build())
        //         .then(Mono.fromRunnable(() -> {
        //             //请求完成回调方法 可以在此完成计算请求耗时等操作
        //         }));
    }

    /**
     * 这里不能用@Order，必须实现Ordered接口
     * 值必须大于10150。原因：Gateway有自己的过滤器，两个比较重要的如下：
     *      RouteToRequestUrlFilter：将根据Route将网关请求转为真实的请求。order = 10000
     *      ReactiveLoadBalancerClientFilter：负载均衡。order = 10150
     */
    @Override
    public int getOrder() {
        return 15000;
    }
}
