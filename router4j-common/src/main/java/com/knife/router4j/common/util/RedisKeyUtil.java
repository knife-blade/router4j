package com.knife.router4j.common.util;

import com.knife.router4j.common.entity.PathRuleRequest;

/**
 * Redis的key工具类
 */
public class RedisKeyUtil {
    private static final String separator = ":";

    /**
     * 组装key
     * @param prefix       前缀
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleKey(String prefix, PathRuleRequest pathRuleRequest) {
        return prefix + pathRuleRequest.getServiceName()
                + separator + pathRuleRequest.getInstanceAddress();
    }
}
