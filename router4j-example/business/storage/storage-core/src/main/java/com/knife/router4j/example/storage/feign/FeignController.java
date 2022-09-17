package com.knife.router4j.example.storage.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class FeignController {

    @GetMapping("/feign/storage/decreaseStorage")
    public void decreaseStorage(@RequestParam("productId")Long productId,
                                @RequestParam("count")Integer count) {
        log.info("库存服务：减少库存。productId: {}, count: {}", productId, count);
    }

    @GetMapping("/feign/storage/decreaseStorageFault")
    public Boolean decreaseStorageFault(@RequestParam("productId")Long productId, @RequestParam("count")Integer count) {
        log.info("库存服务：减少库存刻意抛出错误。productId: {}, count: {}", productId, count);

        int i = 1 / 0;

        return true;
    }
}
