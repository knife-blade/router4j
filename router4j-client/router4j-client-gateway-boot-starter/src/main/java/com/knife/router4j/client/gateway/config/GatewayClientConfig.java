package com.knife.router4j.client.gateway.config;

import com.knife.router4j.client.gateway.filter.SpringCloudGatewayFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class GatewayClientConfig {
    @Bean
    @ConditionalOnClass(GatewayAutoConfiguration.class)
    public SpringCloudGatewayFilter router4jClientFilter() {
        return new SpringCloudGatewayFilter();
    }
}
