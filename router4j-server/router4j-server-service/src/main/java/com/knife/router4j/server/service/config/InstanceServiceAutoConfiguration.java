package com.knife.router4j.server.service.config;

import com.knife.router4j.server.service.InstanceService;
import com.knife.router4j.server.service.impl.NacosImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class InstanceServiceAutoConfiguration {
    @Bean
    @ConditionalOnClass(name = {"com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration"})
    public InstanceService nacosImpl() {
        return new NacosImpl();
    }
}
