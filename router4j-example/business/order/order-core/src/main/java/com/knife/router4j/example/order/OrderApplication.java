package com.knife.router4j.example.order;

import com.knife.router4j.example.common.annotation.CommonApplication;
import com.knife.router4j.example.order.config.MyLoadBalancerClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@CommonApplication
@LoadBalancerClients(defaultConfiguration = MyLoadBalancerClientConfiguration.class)
public class OrderApplication {

    public static void
    main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
