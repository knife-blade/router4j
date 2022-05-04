package com.knife.router4j.common.config;

import com.knife.router4j.common.util.PathRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathRuleConfiguration {
    @Bean
    public PathRule pathRule() {
        return new PathRule();
    }
}
