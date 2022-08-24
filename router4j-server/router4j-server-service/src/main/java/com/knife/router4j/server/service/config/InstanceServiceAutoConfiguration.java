package com.knife.router4j.server.service.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration;
import com.knife.router4j.server.service.ApplicationInfoService;
import com.knife.router4j.server.service.impl.NacosImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class InstanceServiceAutoConfiguration {
    @Bean
    @ConditionalOnClass(NacosDiscoveryAutoConfiguration.class)
    public ApplicationInfoService nacosImpl() {
        return new NacosImpl();
    }

    //todo 添加eureka逻辑
}
