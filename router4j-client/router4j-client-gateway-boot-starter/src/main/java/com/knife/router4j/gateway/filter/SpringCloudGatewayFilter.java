package com.knife.router4j.gateway.filter;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.util.ClientPathRuleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 动态路由
 */
@Slf4j
public class SpringCloudGatewayFilter implements GlobalFilter, Ordered {
    @Autowired
    private ClientPathRuleUtil clientPathRuleUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest originalRequest = exchange.getRequest();

        // 可获得所有请求参数
        // Map<String, String> cachedRequestBody = exchange
        // .getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);

        // 例：http://abc.com:8080/public/example?id=1&name=Tony#ge
        // 则返回：/public/example
        String rawPath = originalRequest.getURI().getRawPath();

        // 获取请求的服务名。route.getUri()示例值：lb://order
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String serviceName = route.getUri().getHost();

        // 从redis中取出对应服务的url，然后用rawPath去匹配
        InstanceInfo matchedInstance = clientPathRuleUtil.findMatchedInstance(serviceName, rawPath);

        URI originUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);

        URI newUri = UriComponentsBuilder.fromUri(originUri)
                .host(matchedInstance.getHost())
                .port(matchedInstance.getPort())
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
