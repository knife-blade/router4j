package com.knife.router4j.common.helper;

import com.knife.router4j.common.entity.InstanceInfo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
