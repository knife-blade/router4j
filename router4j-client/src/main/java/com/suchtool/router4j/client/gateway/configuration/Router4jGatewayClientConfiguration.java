package com.suchtool.router4j.client.gateway.configuration;

import com.suchtool.router4j.client.gateway.filter.Router4jSpringCloudGatewayFilter;
import com.suchtool.router4j.client.gateway.property.Router4jGatewayProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(GatewayAutoConfiguration.class)
@ConditionalOnProperty(value = "suchtool.router4j.gateway.enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class Router4jGatewayClientConfiguration {
    @Bean("com.suchtool.router4j.client.router4jClientFilter")
    public Router4jSpringCloudGatewayFilter router4jClientFilter() {
        return new Router4jSpringCloudGatewayFilter();
    }

    @Bean("com.suchtool.router4j.client.router4jGatewayProperty")
    @ConfigurationProperties(prefix = "suchtool.router4j.gateway")
    public Router4jGatewayProperty router4jGatewayProperty() {
        return new Router4jGatewayProperty();
    }

}
