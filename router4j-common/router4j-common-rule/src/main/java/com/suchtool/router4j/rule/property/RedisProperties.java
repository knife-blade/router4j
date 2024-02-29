package com.suchtool.router4j.rule.property;

import lombok.Data;

import java.time.Duration;

/**
 * redis配置
 */
@Data
public class RedisProperties {
    private String host = "localhost";

    private String password;

    private int port = 6379;

    private int database = 0;

    private boolean ssl;

    private Duration timeout = Duration.ofSeconds(3L);
}