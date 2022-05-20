package com.knife.router4j.common.helper;

import com.knife.router4j.common.entity.PathRuleRequest;
import org.springframework.util.StringUtils;

/**
 * Redis的key工具类
 */
public class RedisKeyHelper {
    public static final String SEPARATOR = ":";

    /**
     * 组装key
     *
     * @param prefix          前缀
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleKey(String prefix, PathRuleRequest pathRuleRequest) {
        return prefix
                + SEPARATOR + pathRuleRequest.getServiceName()
                + SEPARATOR + pathRuleRequest.getInstanceAddress();
    }

    /**
     * 组装key
     *
     * @param prefix      前缀
     * @param serviceName 服务名
     * @return 组装好的key
     */
    public static String assembleKey(String prefix, String serviceName) {
        String tmpServiceName = serviceName;
        if (!StringUtils.hasText(tmpServiceName)) {
            tmpServiceName = "*";
        }

        return prefix + SEPARATOR + tmpServiceName;
    }

    /**
     * 组装key
     *
     * @param prefix          前缀
     * @param serviceName     服务名
     * @param instanceAddress 实例地址
     * @return 组装好的key
     */
    public static String assembleKey(String prefix, String serviceName, String instanceAddress) {
        String tmpServiceName = serviceName;
        String tmpInstanceAddress = instanceAddress;
        if (!StringUtils.hasText(tmpServiceName)) {
            tmpServiceName = "*";
        }

        if (!StringUtils.hasText(tmpInstanceAddress)) {
            tmpInstanceAddress = "*";
        }

        return prefix + SEPARATOR + tmpServiceName + SEPARATOR + tmpInstanceAddress;
    }

    /**
     * 解析出实例地址
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static String parseInstanceAddress(String redisKey) {
        String[] strings = redisKey.split(SEPARATOR);
        int length = strings.length;
        return strings[length - 1];
    }

    /**
     * 解析出服务名
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static String parseServiceName(String redisKey) {
        String[] strings = redisKey.split(SEPARATOR);
        int length = strings.length;
        return strings[length - 2];
    }
}
