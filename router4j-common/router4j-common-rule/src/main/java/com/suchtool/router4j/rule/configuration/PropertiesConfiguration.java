package com.suchtool.router4j.rule.configuration;

import com.suchtool.router4j.rule.property.RedisProperties;
import com.suchtool.router4j.rule.property.RuleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置属性
 */
@Configuration(proxyBeanMethods = false)
public class PropertiesConfiguration {
    @Bean("com.suchtool.router4j.rule.redisProperties")
    @ConfigurationProperties(prefix = "suchtool.router4j.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean("com.suchtool.router4j.rule.ruleProperties")
    @ConfigurationProperties(prefix = "suchtool.router4j.rule")
    public RuleProperties ruleProperties() {
        return new RuleProperties();
    }

}
