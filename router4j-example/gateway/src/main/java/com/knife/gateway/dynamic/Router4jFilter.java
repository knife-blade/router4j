package com.knife.gateway.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
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
@Order(100)
public class Router4jFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();

        // 可获得所有请求参数
        // Map<String, String> cachedRequestBody = exchange
        // .getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);

        //获取域名+端口后的path
        String rawPath = req.getURI().getRawPath();

        // todo 从redis中取出所有url，然后用rawPath去匹配
        String hostAndPort = "http://localhost:9011";
        // 修改uri
        URI uri = UriComponentsBuilder.fromHttpUrl(hostAndPort + rawPath).build().toUri();
        //重新封装request对象
        ServerHttpRequest newRequest = req.mutate().uri(uri).build();

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        boolean encoded = ServerWebExchangeUtils.containsEncodedParts(uri);

        URI routeUri = route.getUri();
        URI mergedUrl = UriComponentsBuilder.fromUri(uri)
                .scheme(routeUri.getScheme())
                .host(routeUri.getHost())
                .port(9011)
                .build(encoded)
                .toUri();

        // NettyRoutingFilter 最终从GATEWAY_REQUEST_URL_ATTR 取出uri对象进行http请求
        // 所以这里要将新的对象覆盖进去
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, mergedUrl);

        return chain.filter(exchange.mutate().request(newRequest).build());

        // 也可以加回调方法
        // return chain.filter(exchange.mutate().request(request).build())
        //         .then(Mono.fromRunnable(() -> {
        //             //请求完成回调方法 可以在此完成计算请求耗时等操作
        //         }));
    }
}
