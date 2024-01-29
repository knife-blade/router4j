package com.suchtool.router4j.client.gateway.config;

import com.suchtool.router4j.client.gateway.filter.Router4jSpringCloudGatewayFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(GatewayAutoConfiguration.class)
@ConditionalOnProperty(value = "router4j.enable", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class Router4jGatewayClientConfig {
    @Bean
    public Router4jSpringCloudGatewayFilter router4jClientFilter() {
        return new Router4jSpringCloudGatewayFilter();
    }
}
