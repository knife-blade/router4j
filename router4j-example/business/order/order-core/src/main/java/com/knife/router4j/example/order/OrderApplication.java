package com.knife.router4j.example.order;

import com.knife.router4j.example.common.annotation.CommonApplication;
import com.knife.router4j.example.order.config.LoadBalancerClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@CommonApplication
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientConfiguration.class)
public class OrderApplication {

    public static void
    main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
