package com.suchtool.router4j.client.feign.loadbalancer;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class Router4jFeignLoadBalancerConfiguration {
    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {

        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new Router4jFeignLoadBalancer(
                loadBalancerClientFactory
                        .getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
