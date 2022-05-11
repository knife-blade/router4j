package com.knife.router4j.common.config;

import com.knife.router4j.common.util.PathRule;
import com.knife.router4j.common.util.UrlUtil;
import com.knife.router4j.common.util.spring.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类的配置
 */
@Configuration
public class UtilConfiguration {
    @Bean
    public PathRule pathRule() {
        return new PathRule();
    }

    @Bean
    public UrlUtil urlUtil() {
        return new UrlUtil();
    }

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }
}
