package com.suchtool.router4j.rule.property;

import lombok.Data;

/**
 * 规则
 */
@Data
public class RuleProperties {
    /**
     * 存到Redis里的路径规则的key的前缀
     */
    private String pathPatternPrefix = "router4j:rule:path-pattern";

    /**
     * 存到Redis里的默认实例的key的前缀
     */
    private String defaultInstancePrefix = "router4j:rule:default-instance";
}