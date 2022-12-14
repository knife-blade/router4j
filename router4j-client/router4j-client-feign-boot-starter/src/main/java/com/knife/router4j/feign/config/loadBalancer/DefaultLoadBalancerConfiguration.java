package com.knife.router4j.feign.config.loadBalancer;

import com.knife.router4j.feign.config.loadBalancer.router4j.Router4jLoadBalancerConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(LoadBalancerClientFactory.class)
@ConditionalOnProperty(value = "router4j.enable", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = Router4jLoadBalancerConfiguration.class)
public class DefaultLoadBalancerConfiguration {
}
