package com.knife.router4j.example.storage.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("storage")
public interface StorageFeignClient {
    @PostMapping("/feign/storage/decreaseStorage")
    void decreaseStorage(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);

    @PostMapping("/feign/storage/decreaseStorageFault")
    Boolean decreaseStorageFault(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
