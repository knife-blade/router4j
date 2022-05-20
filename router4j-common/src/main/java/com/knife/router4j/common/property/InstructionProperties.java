package com.knife.router4j.common.property;

import lombok.Data;

/**
 * 使用指南
 */
@Data
public class InstructionProperties {
    /**
     * 存到Redis里的用法说明的key
     */
    private String cacheKey = "router4j:instruction";
}
