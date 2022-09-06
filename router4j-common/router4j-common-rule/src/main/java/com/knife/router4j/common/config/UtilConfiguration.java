package com.knife.router4j.common.config;

import com.knife.router4j.common.util.*;
import com.knife.router4j.common.util.spring.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类的配置
 */
@Configuration(proxyBeanMethods = false)
public class UtilConfiguration {
    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    public ServerPathRuleUtil serverPathRuleUtil() {
        return new ServerPathRuleUtil();
    }

    @Bean
    public ClientPathRuleUtil clientPathRuleUtil() {
        return new ClientPathRuleUtil();
    }

    @Bean
    public UrlUtil urlUtil() {
        return new UrlUtil();
    }

    @Bean
    public DefaultInstanceUtil defaultInstanceUtil() {
        return new DefaultInstanceUtil();
    }
}
