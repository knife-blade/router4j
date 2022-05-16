package com.knife.router4j.feign.dynamic.config;

import com.knife.router4j.feign.dynamic.config.concret.Router4jDefaultClientConfiguration;
import com.knife.router4j.feign.dynamic.config.concret.Router4jOkHttpClientConfiguration;
import feign.Client;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
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

// @ConditionalOnClass(Feign.class)
// @ConditionalOnBean({LoadBalancerClient.class, LoadBalancerClientFactory.class})
// @AutoConfigureAfter({BlockingLoadBalancerClientAutoConfiguration.class,
//         LoadBalancerAutoConfiguration.class})
// @EnableConfigurationProperties(FeignHttpClientProperties.class)
@Configuration(proxyBeanMethods = false)

// 上边的直接拷贝自FeignLoadBalancerAutoConfiguration.class

@Import({Router4jOkHttpClientConfiguration.class,
        Router4jDefaultClientConfiguration.class})
public class Router4jFeignLoadBalancerAutoConfiguration {

}
