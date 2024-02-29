package com.suchtool.router4j.rule.helper;

import com.suchtool.router4j.rule.constant.RedisConstant;
import org.springframework.util.StringUtils;

/**
 * Redis的key工具类
 */
public class RuleKeyHelper {
    private final String pathPatternPrefix;

    public RuleKeyHelper(String pathPatternPrefix) {
        this.pathPatternPrefix = pathPatternPrefix;
    }

    /**
     * 组装添加key
     *
     * @param applicationName 应用名字
     * @param instanceAddress 实例地址
     * @return 组装好的key
     */
    public String assembleAddKey(String applicationName, String instanceAddress) {
        ValidateUtil.checkApplicationNameValid(applicationName);
        ValidateUtil.checkInstanceAddressValid(instanceAddress);

        String tmpApplicationName = StringUtils.hasText(applicationName)
                ? applicationName
                : RedisConstant.MATCH_ALL;

        String tmpInstanceAddress = StringUtils.hasText(instanceAddress)
                ? instanceAddress
                : RedisConstant.MATCH_ALL;

        return pathPatternPrefix
                + RedisConstant.SEPARATOR + tmpApplicationName
                + RedisConstant.SEPARATOR + tmpInstanceAddress;
    }

    /**
     * 组装删除key（精准删除，不模糊删除）
     *
     * @return 组装好的key
     */
    public String assembleDeleteKeyAccurate(String applicationName, String instanceAddress) {
        return pathPatternPrefix
                + RedisConstant.SEPARATOR + applicationName
                + RedisConstant.SEPARATOR + instanceAddress;
    }

    /**
     * 组装删除key（模糊删除）
     *
     * @return 组装好的key
     */
    public String assembleDeleteKeyFuzzy(String applicationName, String instanceAddress) {
        String applicationNamePattern = StringUtils.hasText(applicationName)
                    && !RedisConstant.MATCH_ALL.equals(applicationName)
                ? RedisConstant.MATCH_ALL + applicationName + RedisConstant.MATCH_ALL
                : RedisConstant.MATCH_ALL;

        String instanceAddressPattern = StringUtils.hasText(instanceAddress)
                    && !RedisConstant.MATCH_ALL.equals(instanceAddress)
                ? RedisConstant.MATCH_ALL + instanceAddress + RedisConstant.MATCH_ALL
                : RedisConstant.MATCH_ALL;

        return pathPatternPrefix
                + RedisConstant.SEPARATOR + applicationNamePattern
                + RedisConstant.SEPARATOR + instanceAddressPattern;
    }

    /**
     * 组装key
     *
     * @param applicationName 服务名
     * @return 组装好的key
     */
    public String assembleSearchKey(String applicationName) {
        return assembleSearchKeyInside(applicationName, RedisConstant.MATCH_ALL);
    }

    /**
     * 组装搜索key
     *
     * @param applicationName 服务名
     * @param instanceAddress 实例地址
     * @return 组装好的key
     */
    public String assembleSearchKey(String applicationName, String instanceAddress) {
        return assembleSearchKeyInside(applicationName, instanceAddress);
    }

    /**
     * 组装搜索key
     */
    private String assembleSearchKeyInside(String applicationName, String instanceAddress) {
        String applicationNamePattern = StringUtils.hasText(applicationName)
                    && !RedisConstant.MATCH_ALL.equals(applicationName)
                ? RedisConstant.MATCH_ALL + applicationName + RedisConstant.MATCH_ALL
                : RedisConstant.MATCH_ALL;

        String instanceAddressPattern = StringUtils.hasText(instanceAddress)
                    && !RedisConstant.MATCH_ALL.equals(instanceAddress)
                ? RedisConstant.MATCH_ALL + instanceAddress + RedisConstant.MATCH_ALL
                : RedisConstant.MATCH_ALL;

        return pathPatternPrefix
                + RedisConstant.SEPARATOR
                + applicationNamePattern
                + RedisConstant.SEPARATOR
                + instanceAddressPattern;
    }

}
