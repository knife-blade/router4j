package com.knife.config;

import com.knife.dynamic.Router4jClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Router4jClientConfig {
    @Bean
    public Router4jClientFilter router4jClientFilter() {
        return new Router4jClientFilter();
    }
}
