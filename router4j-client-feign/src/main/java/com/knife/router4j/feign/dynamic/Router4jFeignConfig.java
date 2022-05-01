package com.knife.router4j.feign.dynamic;

import com.knife.order.dynamic.client.Router4jDefaultModifiedClient;
import feign.Client;
import feign.Request;
import feign.okhttp.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
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
@Configuration
public class Router4jFeignConfig {
    /**
     * 自己实现了一个Client：{@link Router4jDefaultModifiedClient}
     *
     * 为什么提供一个这个Bean就可以了呢？
     * 原因：在Feign的接口上打断点，追到如下位置：{@link FeignBlockingLoadBalancerClient#execute(Request, Request.Options)}
     *      它最后一行代码用到了Client delegate，它来自于构造函数：{@link FeignBlockingLoadBalancerClient#FeignBlockingLoadBalancerClient(Client, LoadBalancerClient, LoadBalancerProperties, LoadBalancerClientFactory)}
     *      此构造函数被调用的地方：DefaultFeignLoadBalancerConfiguration#feignClient(xxx)
     *      它上边有个：@ConditionalOnMissingBean
     *      那么：我们就可以手动构造一个替换它！
     */
    @Bean
    @ConditionalOnMissingClass({"feign.okhttp.OkHttpClient", "feign.httpclient.ApacheHttpClient"})
    @Conditional(OnRetryNotEnabledCondition.class)
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              LoadBalancerProperties properties,
                              LoadBalancerClientFactory loadBalancerClientFactory) {
        return new FeignBlockingLoadBalancerClient(
                new Router4jDefaultModifiedClient(null, null),
                loadBalancerClient, properties, loadBalancerClientFactory);
    }

    /**
     * 重要的类：OkHttpFeignLoadBalancerConfiguration#feignClient(xxx)}
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(OnRetryNotEnabledCondition.class)
    public Client feignClient(okhttp3.OkHttpClient okHttpClient,
                              LoadBalancerClient loadBalancerClient,
                              LoadBalancerProperties properties,
                              LoadBalancerClientFactory loadBalancerClientFactory) {
        OkHttpClient delegate = new OkHttpClient(okHttpClient);
        return new FeignBlockingLoadBalancerClient(delegate, loadBalancerClient, properties, loadBalancerClientFactory);
    }
}
