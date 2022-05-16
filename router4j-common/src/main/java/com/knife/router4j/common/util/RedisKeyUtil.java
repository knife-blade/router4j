package com.knife.router4j.common.util;

import com.knife.router4j.common.entity.InstanceInfo;

/**
 * Redis的key工具类
 */
public class RedisKeyUtil {
    private static final String separator = ":";

    /**
     * 组装key
     * @param prefix       前缀
     * @param instanceInfo 实例信息
     * @return 组装好的key
     */
    public static String assembleKey(String prefix, InstanceInfo instanceInfo) {
        return prefix + separator + instanceInfo.getServiceName()
                + separator + instanceInfo.instanceAddressWithProtocol();
    }
}
