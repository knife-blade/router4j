package com.knife.order.dynamic;

import com.knife.order.dynamic.client.Router4jDefaultModifiedClient;
import feign.Client;
import feign.Request;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Router4jFeignConfig {
    /**
     * 为什么提供一个这个Bean就可以了呢？
     * 原因：在Feign的接口上打断点，追到如下位置：{@link FeignBlockingLoadBalancerClient#execute(Request, Request.Options)}
     *      它最后一行代码用到了Client delegate，它来自于构造函数：{@link FeignBlockingLoadBalancerClient#FeignBlockingLoadBalancerClient(Client, LoadBalancerClient, LoadBalancerProperties, LoadBalancerClientFactory)}
     *      此构造函数被调用的地方：DefaultFeignLoadBalancerConfiguration#feignClient(xxx)
     *      它上边有个：@ConditionalOnMissingBean
     *      那么：我们就可以手动构造一个替换它！
     */
    @Bean
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              LoadBalancerProperties properties,
                              LoadBalancerClientFactory loadBalancerClientFactory) {
        return new FeignBlockingLoadBalancerClient(
                new Router4jDefaultModifiedClient(null, null),
                loadBalancerClient, properties, loadBalancerClientFactory);
    }
}
