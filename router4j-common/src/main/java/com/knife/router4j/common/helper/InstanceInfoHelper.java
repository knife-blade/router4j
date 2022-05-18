package com.knife.router4j.common.helper;

import com.knife.router4j.common.entity.InstanceInfo;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 实例信息帮助类
 */
public class InstanceInfoHelper {
    public static InstanceInfo assembleInstanceAddress(String urlAddress) {
        URL url = null;
        try {
            url = new URL(urlAddress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setProtocol(url.getProtocol());
        instanceInfo.setHost(url.getHost());
        instanceInfo.setPort(url.getPort());
        return instanceInfo;
    }
}
