package com.knife.router4j.common.property;

import lombok.Data;

/**
 * 规则的前缀
 */
@Data
public class PrefixProperties {
    /**
     * 存放到Redis里的key的前缀
     */
    private String setting = "router4j:rule:prefix:setting:";

    /**
     * 存放到Redis里的key的前缀
     */
    private String defaultInstance = "router4j:rule:prefix:default";
}