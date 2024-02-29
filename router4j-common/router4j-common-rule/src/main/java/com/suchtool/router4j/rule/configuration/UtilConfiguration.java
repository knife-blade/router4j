package com.suchtool.router4j.rule.configuration;

import com.suchtool.router4j.rule.util.ClientPathRuleUtil;
import com.suchtool.router4j.rule.util.DefaultInstanceUtil;
import com.suchtool.router4j.rule.util.ServerPathRuleUtil;
import com.suchtool.router4j.rule.util.UrlUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类的配置
 */
@Configuration(proxyBeanMethods = false)
public class UtilConfiguration {
    @Bean("suchtool.router4j.common.rule.serverPathRuleUtil")
    public ServerPathRuleUtil serverPathRuleUtil() {
        return new ServerPathRuleUtil();
    }

    @Bean("suchtool.router4j.common.rule.clientPathRuleUtil")
    public ClientPathRuleUtil clientPathRuleUtil() {
        return new ClientPathRuleUtil();
    }

    @Bean("suchtool.router4j.common.rule.urlUtil")
    public UrlUtil urlUtil() {
        return new UrlUtil();
    }

    @Bean("suchtool.router4j.common.rule.defaultInstanceUtil")
    public DefaultInstanceUtil defaultInstanceUtil() {
        return new DefaultInstanceUtil();
    }
}
