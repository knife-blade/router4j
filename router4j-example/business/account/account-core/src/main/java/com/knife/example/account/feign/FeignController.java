package com.knife.example.account.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
public class FeignController {

    @PostMapping("/feign/account/decreaseMoney")
    public void decreaseMoney(@RequestParam("userId")Long userId, @RequestParam("money")BigDecimal money) {
        // 扣减金额
        log.info("账户服务：扣减用户{}金额{}元", userId, money);
    }
}
