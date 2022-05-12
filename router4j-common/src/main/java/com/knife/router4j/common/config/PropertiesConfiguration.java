package com.knife.router4j.common.config;

import com.knife.router4j.common.property.RedisProperties;
import com.knife.router4j.common.property.PrefixProperties;
import com.knife.router4j.common.property.RuleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置属性
 */
@Configuration
public class PropertiesConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "router4j.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "router4j.rule")
    public RuleProperties ruleProperties() {
        return new RuleProperties();
    }
}
