package com.suchtool.router4j.common.instance.config;

import com.suchtool.router4j.common.instance.applicationInfo.impl.ApplicationInfoServiceImpl;
import com.suchtool.router4j.common.instance.applicationInfo.ApplicationInfoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean(DiscoveryClient.class)
@Configuration(proxyBeanMethods = false)
public class InstanceServiceAutoConfiguration {
    @Bean("com.suchtool.router4j.applicationInfoService")
    public ApplicationInfoService applicationInfoService() {
        return new ApplicationInfoServiceImpl();
    }
}
