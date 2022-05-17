package com.knife.router4j.common.entity;

import lombok.Data;

/**
 * 路径规则请求
 */
@Data
public class PathRuleRequest {
    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 实例地址
     */
    private String instanceAddress;

    /**
     * 路径模板
     */
    private String pathPattern;
}
