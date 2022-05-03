package com.knife.router4j.feign.dynamic.config.concret;

import com.knife.router4j.feign.dynamic.client.Router4jDefaultClient;
import com.knife.router4j.feign.dynamic.client.Router4jOkHttpClient;
import feign.Client;
import feign.Request;
import feign.okhttp.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.OnRetryNotEnabledCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 重要的类：
 *      Feign自动配置：{@link FeignAutoConfiguration}
 *      Feign的Client接口：{@link Client}
 */
@Configuration(proxyBeanMethods = false)
// 没有OkHttpClient和ApacheHttpClient时才生效
@ConditionalOnMissingClass({"feign.okhttp.OkHttpClient", "feign.httpclient.ApacheHttpClient"})
@EnableConfigurationProperties(LoadBalancerProperties.class)
public class Router4jDefaultClientConfiguration {
    /**
     * 自己实现了一个Client：{@link Router4jDefaultClient}
     *
     * 为什么提供一个这个Bean就可以了呢？
     * 原因：在Feign的接口上打断点，追到如下位置：{@link FeignBlockingLoadBalancerClient#execute(Request, Request.Options)}
     *      它最后一行代码用到了Client delegate，它来自于构造函数：{@link FeignBlockingLoadBalancerClient#FeignBlockingLoadBalancerClient(Client, LoadBalancerClient, LoadBalancerProperties, LoadBalancerClientFactory)}
     *      此构造函数被调用的地方：DefaultFeignLoadBalancerConfiguration#feignClient(xxx)
     *      它上边有个：@ConditionalOnMissingBean
     *      那么：我们就可以手动构造一个替换它！
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(OnRetryNotEnabledCondition.class)
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              LoadBalancerProperties properties,
                              LoadBalancerClientFactory loadBalancerClientFactory) {
        return new FeignBlockingLoadBalancerClient(
                new Router4jDefaultClient(null, null),
                loadBalancerClient, properties, loadBalancerClientFactory);
    }
}
