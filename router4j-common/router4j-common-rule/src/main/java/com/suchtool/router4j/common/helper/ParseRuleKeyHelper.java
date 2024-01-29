package com.suchtool.router4j.common.helper;

import com.suchtool.router4j.common.constant.RedisConstant;

/**
 * 解析规则的key的工具类
 */
public class ParseRuleKeyHelper {

    /**
     * 解析出服务名
     *
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static String parseApplicationName(String redisKey) {
        String[] strings = redisKey.split(RedisConstant.SEPARATOR);
        int length = strings.length;
        return strings[length - 3];
    }

    /**
     * 解析出实例地址
     *
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static String parseInstanceAddress(String redisKey) {
        String[] strings = redisKey.split(RedisConstant.SEPARATOR);
        int length = strings.length;
        return strings[length - 2] + ":" + strings[length - 1];
    }

    /**
     * 解析出实例的IP
     *
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static String parseInstanceIp(String redisKey) {
        String instanceAddress = parseInstanceAddress(redisKey);
        String[] strings = instanceAddress.split(":");
        return strings[0];
    }

    /**
     * 解析出端口
     *
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static int parsePort(String redisKey) {
        String instanceAddress = parseInstanceAddress(redisKey);
        String[] strings = instanceAddress.split(":");
        return Integer.parseInt(strings[1]);
    }
}
