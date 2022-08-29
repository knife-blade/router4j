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
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleAddKey(PathRuleRequest pathRuleRequest) {
        String applicationName = StringUtils.hasText(pathRuleRequest.getApplicationName())
                ? pathRuleRequest.getApplicationName()
                : RedisConstant.EMPTY_PLACEHOLDER;

        String instanceAddress = StringUtils.hasText(pathRuleRequest.getInstanceAddress())
                ? pathRuleRequest.getInstanceAddress()
                : RedisConstant.EMPTY_PLACEHOLDER;

        return pathPatternPrefix
                + RedisConstant.SEPARATOR + applicationName
                + RedisConstant.SEPARATOR + instanceAddress;
    }

    /**
     * 组装删除key（精准删除，不模糊删除）
     *
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleDeleteKey(PathRuleRequest pathRuleRequest) {
        String applicationNamePattern = StringUtils.hasText(pathRuleRequest.getApplicationName())
                ? pathRuleRequest.getApplicationName()
                : RedisConstant.SEARCH_ALL;

        String instanceAddressPattern = StringUtils.hasText(pathRuleRequest.getInstanceAddress())
                ? pathRuleRequest.getInstanceAddress()
                : RedisConstant.SEARCH_ALL;

        return pathPatternPrefix
                + RedisConstant.SEPARATOR + applicationNamePattern
                + RedisConstant.SEPARATOR + instanceAddressPattern;
    }

    /**
     * 组装搜索key
     *
     * @param pathRuleRequest 实例信息
     * @return 组装好的key
     */
    public static String assembleSearchKey(PathRuleRequest pathRuleRequest) {
        return assembleSearchKeyInside(
                pathRuleRequest.getApplicationName(),
                pathRuleRequest.getInstanceAddress());
    }

    /**
     * 组装key
     *
     * @param applicationName 服务名
     * @return 组装好的key
     */
    public static String assembleSearchKey(String applicationName) {
        return assembleSearchKeyInside(applicationName, null);
    }

    /**
     * 组装搜索key
     *
     * @param applicationName     服务名
     * @param instanceAddress 实例地址
     * @return 组装好的key
     */
    public static String assembleSearchKey(String applicationName, String instanceAddress) {
        return assembleSearchKeyInside(applicationName, instanceAddress);
    }

    /**
     * 组装搜索key
     */
    private static String assembleSearchKeyInside(String applicationName, String instanceAddress) {
        String applicationNamePattern = StringUtils.hasText(applicationName)
                ? RedisConstant.SEARCH_ALL + applicationName + RedisConstant.SEARCH_ALL
                : RedisConstant.SEARCH_ALL;

        String instanceAddressPattern = StringUtils.hasText(instanceAddress)
                ? RedisConstant.SEARCH_ALL + instanceAddress + RedisConstant.SEARCH_ALL
                : RedisConstant.SEARCH_ALL;

        return pathPatternPrefix 
                + RedisConstant.SEPARATOR 
                + applicationNamePattern
                + RedisConstant.SEPARATOR 
                + instanceAddressPattern;
    }

    /**
     * 组装删除key
     */
    public static String assembleDeleteKey(String applicationName, String instanceAddress) {
        String applicationNamePattern = StringUtils.hasText(applicationName)
                ? applicationName
                : RedisConstant.SEARCH_ALL;

        String instanceAddressPattern = StringUtils.hasText(instanceAddress)
                ? instanceAddress
                : RedisConstant.SEARCH_ALL;

        return pathPatternPrefix
                + RedisConstant.SEPARATOR + applicationNamePattern
                + RedisConstant.SEPARATOR + instanceAddressPattern;
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
    public static String parseApplicationName(String redisKey) {
        String[] strings = redisKey.split(RedisConstant.SEPARATOR);
        int length = strings.length;
        return strings[length - 2];
    }
}
