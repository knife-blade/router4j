package com.knife.common.annotation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// @MapperScan("com.knife.**.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.knife.**.api")

public @interface CommonApplication {
    @AliasFor(annotation = SpringBootApplication.class)
    String[] scanBasePackages() default "com.knife.**";
}
