package com.knife.router4j.common.helper;

import com.knife.router4j.common.common.entity.InstanceInfo;

import java.net.URI;

/**
 * 实例信息帮助类
 */
public class InstanceInfoHelper {
    public static InstanceInfo assembleInstanceAddress(String instanceAddress) {
        URI uri = URI.create(instanceAddress);
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setProtocol(uri.getScheme());
        instanceInfo.setHost(uri.getHost());
        instanceInfo.setPort(uri.getPort());
        return instanceInfo;
    }
}
