package com.knife.router4j.common.helper;

import com.knife.router4j.common.constant.RedisKeySeparator;
import com.knife.router4j.common.entity.PathRuleRequest;
import org.springframework.util.StringUtils;

/**
 * Redis的key工具类
 */
public class RedisKeyHelper {

    /**
     * 组装key
     *
     * @param prefix          前缀
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleKey(String prefix, PathRuleRequest pathRuleRequest) {
        return prefix + RedisKeySeparator.SEPARATOR + pathRuleRequest.getServiceName()
                + RedisKeySeparator.INSTANCE_ADDRESS_SEPARATOR + pathRuleRequest.getInstanceAddress();
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

        return prefix + RedisKeySeparator.SEPARATOR + tmpServiceName;
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

        return prefix + RedisKeySeparator.SEPARATOR + tmpServiceName
                + RedisKeySeparator.INSTANCE_ADDRESS_SEPARATOR + tmpInstanceAddress;
    }

    /**
     * 解析出实例地址
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static String parseInstanceAddress(String redisKey) {
        String[] strings = redisKey.split(RedisKeySeparator.INSTANCE_ADDRESS_SEPARATOR);
        return strings[1];
    }
}
