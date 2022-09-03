package com.knife.router4j.common.common.entity;

import lombok.Data;

/**
 * 实例的信息
 */
@Data
public class InstanceInfo {
    /**
     * 应用名字
     */
    private String applicationName;

    /**
     * 协议
     */
    private String protocol = "http";

    /**
     * 主机
     */
    private String host;

    /**
     * 端口号
     */
    private int port;

    /**
     * 获得实例地址（带协议）
     * @return 实例地址
     */
    public String instanceAddressWithProtocol() {
        return protocol + "://" + host + ":" + port;
    }

    /**
     * 获得实例地址（不带协议）
     * @return 实例地址
     */
    public String instanceAddressWithoutProtocol() {
        return host + ":" + port;
    }
}
