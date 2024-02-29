package com.suchtool.router4j.client.feign.configuration;

import com.suchtool.router4j.client.feign.loadbalancer.Router4jFeignLoadBalancerConfiguration;
import com.suchtool.router4j.client.feign.property.Router4jFeignProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnMissingClass("org.springframework.cloud.gateway.config.GatewayAutoConfiguration")
@ConditionalOnClass(LoadBalancerClientFactory.class)
@ConditionalOnProperty(value = "suchtool.router4j.feign.enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = Router4jFeignLoadBalancerConfiguration.class)
public class Router4jFeignClientConfiguration {
    @Bean("com.suchtool.router4j.client.router4jFeignProperty")
    @ConfigurationProperties(prefix = "suchtool.router4j.feign")
    public Router4jFeignProperty router4jFeignProperty() {
        return new Router4jFeignProperty();
    }
}
