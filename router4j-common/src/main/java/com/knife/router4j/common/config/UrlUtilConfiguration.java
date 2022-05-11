package com.knife.router4j.common.config;

import com.knife.router4j.common.util.UrlUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UrlUtilConfiguration {
    @Bean
    public UrlUtil urlUtil() {
        return new UrlUtil();
    }
}
