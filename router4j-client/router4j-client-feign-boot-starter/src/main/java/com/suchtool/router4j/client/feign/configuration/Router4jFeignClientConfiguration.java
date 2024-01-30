package com.suchtool.router4j.client.feign.configuration;

import com.suchtool.router4j.client.feign.loadbalancer.Router4jLoadBalancerConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(LoadBalancerClientFactory.class)
@ConditionalOnProperty(value = "router4j.enable", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = Router4jLoadBalancerConfiguration.class)
public class Router4jFeignClientConfiguration {
}
