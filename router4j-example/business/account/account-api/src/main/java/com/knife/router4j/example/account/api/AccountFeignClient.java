package com.suchtool.router4j.example.account.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("account")
public interface AccountFeignClient {
    @GetMapping("/feign/account/decreaseMoney")
    void decreaseMoney(@RequestParam("userId")Long userId, @RequestParam("money") BigDecimal money);
}
