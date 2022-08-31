package com.knife.router4j.common.instance.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration;
import com.knife.router4j.common.instance.impl.NacosImpl;
import com.knife.router4j.common.instance.ApplicationInfoService;
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
