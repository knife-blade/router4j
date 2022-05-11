package com.knife.router4j.common.property;

import lombok.Data;

import java.time.Duration;

/**
 * redis配置
 */
@Data
public class RedisProperties {
    private int database = 0;

    private String url;

    private String host = "localhost";

    private String password;

    private int port = 6379;

    private boolean ssl;

    private Duration timeout;
}