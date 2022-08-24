package com.knife.router4j.common.helper;

import com.knife.router4j.common.constant.RedisConstant;
import org.springframework.util.StringUtils;

/**
 * Redis的key工具类
 */
public class RedisValueHelper {

    /**
     * 组装value
     *
     * @param serviceName     服务名
     * @param instanceAddress 实例地址。例如：127.0.0.1:8080
     * @return 组装好的key
     */
    public static String assembleValue(String serviceName, String instanceAddress) {
        String tmpServiceName = serviceName;
        String tmpInstanceAddress = instanceAddress;
        if (!StringUtils.hasText(tmpServiceName)) {
            tmpServiceName = "*";
        }

        if (!StringUtils.hasText(tmpInstanceAddress)) {
            tmpInstanceAddress = "*";
        }

        return tmpServiceName + RedisConstant.SEPARATOR + tmpInstanceAddress;
    }

    /**
     * 解析出实例地址
     * @param redisValue redis的value
     * @return 实例地址
     */
    public static String parseInstanceAddress(String redisValue) {
        String[] strings = redisValue.split(RedisConstant.SEPARATOR);
        return strings[1] + strings[2];
    }

    /**
     * 解析出服务名
     * @param redisValue redis的value
     * @return 实例地址
     */
    public static String parseServiceName(String redisValue) {
        String[] strings = redisValue.split(RedisConstant.SEPARATOR);
        return strings[0];
    }
}
