package com.suchtool.router4j.client.feign.configuration;

import com.suchtool.router4j.client.feign.loadbalancer.Router4jLoadBalancerConfiguration;
import com.suchtool.router4j.client.feign.property.Router4jFeignProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(LoadBalancerClientFactory.class)
@ConditionalOnProperty(value = "suchtool.router4j.feign.enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = Router4jLoadBalancerConfiguration.class)
public class Router4jFeignClientConfiguration {
    @Bean("com.suchtool.router4j.router4jFeignProperty")
    @ConfigurationProperties(prefix = "suchtool.router4j.feign")
    public Router4jFeignProperty router4jFeignProperty() {
        return new Router4jFeignProperty();
    }
}
