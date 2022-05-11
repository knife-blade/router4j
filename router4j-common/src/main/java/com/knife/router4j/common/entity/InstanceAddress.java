package com.knife.router4j.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 实例的信息
 */
@Data
public class InstanceAddress {
    private String protocol = "http";

    private String host;

    private int port;

    public String addressWithProtocol() {
        return protocol + "://" + host + ":" + port;
    }

    public String addressWithoutProtocol() {
        return host + ":" + port;
    }
}
