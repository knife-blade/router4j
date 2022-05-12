package com.knife.router4j.common.property;

import lombok.Data;

/**
 * 规则
 */
@Data
public class RuleProperties {
    /**
     * 是否开启路由功能
     */
    private Boolean enable = true;

    /**
     * 存到Redis里的key的前缀
     */
    private PrefixProperties prefix;
}