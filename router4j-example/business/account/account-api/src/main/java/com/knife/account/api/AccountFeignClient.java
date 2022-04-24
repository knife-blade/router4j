package com.knife.account.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("account")
public interface AccountFeignClient {
    @PostMapping("/feign/account/decreaseMoney")
    void decreaseMoney(@RequestParam("userId")Long userId, @RequestParam("money") BigDecimal money);
}
