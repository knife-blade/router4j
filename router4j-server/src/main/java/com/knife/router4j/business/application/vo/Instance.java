package com.knife.router4j.business.application.vo;

import lombok.Data;

/**
 * 服务的实例
 */
@Data
public class Instance {
    /**
     * 服务的ID（一般是服务名）
     */
    private String serviceId;

    /**
     * IP
     */
    private String host;

    /**
     * 端口号
     */
    private int port;
}