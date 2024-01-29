package com.suchtool.router4j.common.config;

import com.suchtool.router4j.common.util.*;
import com.suchtool.router4j.common.util.ClientPathRuleUtil;
import com.suchtool.router4j.common.util.DefaultInstanceUtil;
import com.suchtool.router4j.common.util.ServerPathRuleUtil;
import com.suchtool.router4j.common.util.UrlUtil;
import com.suchtool.router4j.common.util.spring.ApplicationContextHolder;
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
