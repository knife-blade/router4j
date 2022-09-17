package com.knife.router4j.example.storage.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("storage")
public interface StorageFeignClient {
    @GetMapping("/feign/storage/decreaseStorage")
    void decreaseStorage(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);

    @GetMapping("/feign/storage/decreaseStorageFault")
    Boolean decreaseStorageFault(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
