package com.knife.router4j.common.helper;

import com.knife.router4j.common.constant.RedisConstant;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.util.spring.ApplicationContextHolder;
import org.springframework.util.StringUtils;

/**
 * Redis的rule的key工具类
 */
public class RuleKeyHelper {
    private static final String pathPatternPrefix;

    static {
        pathPatternPrefix = ApplicationContextHolder.getContext()
                .getBean(RuleProperties.class)
                .getPathPatternPrefix();
    }

    /**
     * 组装添加key
     *
     * @param prefix          前缀
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleAddKey(String prefix, PathRuleRequest pathRuleRequest) {
        String serviceName = StringUtils.hasText(pathRuleRequest.getServiceName())
                ? pathRuleRequest.getServiceName()
                : RedisConstant.EMPTY_PLACEHOLDER;

        String instanceAddress = StringUtils.hasText(pathRuleRequest.getInstanceAddress())
                ? pathRuleRequest.getInstanceAddress()
                : RedisConstant.EMPTY_PLACEHOLDER;

        return prefix
                + RedisConstant.SEPARATOR + serviceName
                + RedisConstant.SEPARATOR + instanceAddress;
    }

    /**
     * 组装搜索key
     *
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleSearchKey(PathRuleRequest pathRuleRequest) {
        String serviceName = StringUtils.hasText(pathRuleRequest.getServiceName())
                ? pathRuleRequest.getServiceName()
                : RedisConstant.SEARCH_ALL;

        String instanceAddress = StringUtils.hasText(pathRuleRequest.getInstanceAddress())
                ? pathRuleRequest.getInstanceAddress()
                : RedisConstant.SEARCH_ALL;

        return pathPatternPrefix
                + RedisConstant.SEPARATOR + serviceName
                + RedisConstant.SEPARATOR + instanceAddress;
    }

    /**
     * 组装key
     *
     * @param serviceName 服务名
     * @return 组装好的key
     */
    public static String assembleSearchKey(String serviceName) {
        String tmpServiceName = StringUtils.hasText(serviceName)
                ? serviceName
                : RedisConstant.SEARCH_ALL;

        return pathPatternPrefix + RedisConstant.SEPARATOR + tmpServiceName;
    }

    /**
     * 组装key
     *
     * @param serviceName     服务名
     * @param instanceAddress 实例地址
     * @return 组装好的key
     */
    public static String assembleSearchKey(String serviceName, String instanceAddress) {
        String tmpServiceName = StringUtils.hasText(serviceName)
                ? serviceName
                : RedisConstant.SEARCH_ALL;

        String tmpInstanceAddress = StringUtils.hasText(instanceAddress)
                ? instanceAddress
                : RedisConstant.SEARCH_ALL;

        return pathPatternPrefix +
                RedisConstant.SEPARATOR + tmpServiceName +
                RedisConstant.SEPARATOR + tmpInstanceAddress;
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
        return strings[length - 1];
    }

    /**
     * 解析出服务名
     *
     * @param redisKey redis的key
     * @return 实例地址
     */
    public static String parseServiceName(String redisKey) {
        String[] strings = redisKey.split(RedisConstant.SEPARATOR);
        int length = strings.length;
        return strings[length - 2];
    }
}
