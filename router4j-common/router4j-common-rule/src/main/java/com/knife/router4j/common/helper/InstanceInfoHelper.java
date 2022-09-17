package com.knife.router4j.common.helper;

import com.knife.router4j.common.common.entity.InstanceInfo;

import java.net.URI;

/**
 * 实例信息帮助类
 */
public class InstanceInfoHelper {
    public static InstanceInfo assembleInstanceAddress(String instanceAddress) {
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setProtocol("http");
        instanceInfo.setHost(ParseRuleKeyHelper.parseInstanceIp(instanceAddress));
        instanceInfo.setPort(ParseRuleKeyHelper.parsePort(instanceAddress));
        return instanceInfo;
    }
}
