package com.knife.router4j.common.property;

import lombok.Data;

import java.time.Duration;

/**
 * 规则配置
 */
@Data
public class RuleProperties {
    /**
     * 存放到Redis里的key的前缀
     */
    private String prefix = "router4j:rule:cache:prefix:";
}