package com.knife.router4j.feign.config;

import com.knife.router4j.feign.config.concret.Router4jApacheHttpClientConfiguration;
import com.knife.router4j.feign.config.concret.Router4jDefaultClientConfiguration;
import com.knife.router4j.feign.config.concret.Router4jOkHttpClientConfiguration;
import feign.Client;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 重要的类：
 * Feign自动配置：{@link FeignAutoConfiguration}
 * Feign的Client接口：{@link Client}
 * LoadBalancer的自动配置：{@link FeignLoadBalancerAutoConfiguration}
 */
// 这行是自己加的
@AutoConfigureBefore({FeignLoadBalancerAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
@Import({Router4jApacheHttpClientConfiguration.class,
        Router4jOkHttpClientConfiguration.class,
        Router4jDefaultClientConfiguration.class})
public class Router4jFeignLoadBalancerAutoConfiguration {

}
