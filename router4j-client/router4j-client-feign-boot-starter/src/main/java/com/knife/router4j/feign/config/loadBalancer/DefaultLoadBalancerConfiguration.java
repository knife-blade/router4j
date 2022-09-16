package com.knife.router4j.feign.config.loadBalancer;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = Router4jLoadBalancerConfiguration.class)
public class DefaultLoadBalancerConfiguration {
}
