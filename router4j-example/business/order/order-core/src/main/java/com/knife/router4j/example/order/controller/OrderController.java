package com.knife.router4j.example.order.controller;

import com.knife.router4j.example.common.entity.Result;
import com.knife.router4j.example.account.api.AccountFeignClient;
import com.knife.router4j.example.order.entity.Order;
import com.knife.router4j.example.storage.api.StorageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://localhost:9011/order/create/?userId=1&productId=1&count=10&money=100
 * http://localhost:9011/order/createFault/?userId=1&productId=1&count=10&money=100
 */

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private StorageFeignClient storageFeignClient;

    // 正常流程
    @GetMapping("create")
    public Result create(Order order) {
        log.info("订单服务：创建订单：{}", order);

        storageFeignClient.decreaseStorage(order.getProductId(), order.getCount());
        accountFeignClient.decreaseMoney(order.getUserId(), order.getMoney());

        return new Result().message("创建订单成功");
    }

    // 在减库存时刻意抛出异常
    @PostMapping("createFault")
    public Result createFault(Order order) {
        log.error("订单服务：创建订单刻意抛出异常：{}", order);

        storageFeignClient.decreaseStorageFault(order.getProductId(), order.getCount());
        accountFeignClient.decreaseMoney(order.getUserId(), order.getMoney());

        return new Result().message("创建订单成功");
    }
}


