package com.knife.router4j.common.entity;

import lombok.Data;

/**
 * 默认实例的信息
 */
@Data
public class DefaultInstanceInfo {
    /**
     * 应用名字
     */
    private String applicationName;

    /**
     * 实例地址
     */
    private String instanceAddress;

    /**
     * 是否强制
     */
    private Boolean isForceRoute;
}
