package com.suchtool.router4j.common.instance.configuration;

import com.alibaba.cloud.nacos.NacosServiceAutoConfiguration;
import com.suchtool.router4j.common.instance.applicationInfo.service.impl.DefaultApplicationInfoServiceImpl;
import com.suchtool.router4j.common.instance.applicationInfo.service.ApplicationInfoService;
import com.suchtool.router4j.common.instance.applicationInfo.service.impl.NacosApplicationInfoServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean(DiscoveryClient.class)
@Configuration(proxyBeanMethods = false)
public class InstanceServiceAutoConfiguration {

    @ConditionalOnMissingClass("com.alibaba.cloud.nacos.NacosServiceAutoConfiguration")
    @Configuration(proxyBeanMethods = false)
    protected static class DefaultApplicationInfoServiceImplConfiguration {
        @Bean("com.suchtool.router4j.defaultApplicationInfoServiceImpl")
        public ApplicationInfoService defaultApplicationInfoServiceImpl() {
            return new DefaultApplicationInfoServiceImpl();
        }
    }

    @ConditionalOnClass(NacosServiceAutoConfiguration.class)
    @Configuration(proxyBeanMethods = false)
    protected static class NacosApplicationInfoServiceImplConfiguration {
        @Bean("com.suchtool.router4j.nacosApplicationInfoServiceImplConfiguration")
        public ApplicationInfoService nacosApplicationInfoServiceImplConfiguration() {
            return new NacosApplicationInfoServiceImpl();
        }
    }
}
