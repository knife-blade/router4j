package com.knife.router4j.client.gateway.config;

import com.knife.router4j.client.gateway.filter.Router4jSpringCloudGatewayFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class Router4jGatewayClientConfig {
    @Bean
    @ConditionalOnClass(GatewayAutoConfiguration.class)
    public Router4jSpringCloudGatewayFilter router4jClientFilter() {
        return new Router4jSpringCloudGatewayFilter();
    }
}
