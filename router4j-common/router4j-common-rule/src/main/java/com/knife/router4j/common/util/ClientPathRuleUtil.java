package com.knife.router4j.common.util;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.constant.RedisConstant;
import com.knife.router4j.common.helper.InstanceInfoHelper;
import com.knife.router4j.common.helper.ParseRuleKeyHelper;
import com.knife.router4j.common.helper.PathMatchHelper;
import com.knife.router4j.common.helper.RuleKeyHelper;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import com.knife.router4j.common.util.spring.ApplicationContextHolder;
import org.redisson.api.RKeys;
import org.redisson.api.RList;

import java.util.HashMap;
import java.util.Map;

/**
 * 路径的规则
 * 客户端使用：gateway、feign
 */
public class ClientPathRuleUtil {
    private static final RuleKeyHelper ruleKeyHelper;

    static {
        String keyPrefix = ApplicationContextHolder.getContext()
                .getBean(RuleProperties.class)
                .getPathPatternPrefix();
        ruleKeyHelper = new RuleKeyHelper(keyPrefix);
    }

    /**
     * 通过路径找到规则中的实例（用于网关和feign）
     *
     * @param path 路径。例如：/order/add
     * @return 实例地址。例如：127.0.0.1:8080
     */
    public InstanceInfo findMatchedInstance(String path) {
        return findMatchedInstance(RedisConstant.MATCH_ALL, path);
    }

    /**
     * 通过路径找到规则中的实例（用于网关和feign）
     *
     * @param applicationName 服务名
     * @param path            路径。例如：/order/add
     * @return 实例信息
     */
    public InstanceInfo findMatchedInstance(String applicationName, String path) {
        // 如果没开启rule功能，则直接返回null
        // if (!ruleProperties.getEnable()) {
        //     return null;
        // }

        // key：实例地址  value：最具体的pattern
        Map<String, String> matchedMap = new HashMap<>();

        RKeys keys = RedissonHolder.getRedissonClient().getKeys();

        String keyPattern = ruleKeyHelper.assembleSearchKey(applicationName);

        // 模糊查找出所有实例的key
        Iterable<String> ruleKeys = keys.getKeysByPattern(keyPattern);

        for (String ruleKey : ruleKeys) {
            // 取出每个实例的所有指定好的路径规则
            RList<String> pathPatterns = RedissonHolder.getRedissonClient().getList(ruleKey);

            // 找出每个实例里匹配的最长的pathPattern
            for (String pathPattern : pathPatterns) {
                if (PathMatchHelper.matchForRoute(pathPattern, path)) {
                    String patternOfMap = matchedMap.get(ruleKey);
                    if (patternOfMap == null
                            || pathPattern.length() > patternOfMap.length()) {
                        matchedMap.put(ruleKey, pathPattern);
                    }
                }
            }
        }

        // 找出所有的pathPattern里最长的
        String matchedKey = null;
        String longestPath = "";

        for (Map.Entry<String, String> entry : matchedMap.entrySet()) {
            if (entry.getValue().length() > longestPath.length()) {
                longestPath = entry.getValue();
                matchedKey = entry.getKey();
            }
        }

        if (matchedKey == null) {
            return null;
        } else {
            String instanceAddress = ParseRuleKeyHelper.parseInstanceAddress(matchedKey);
            return InstanceInfoHelper.assembleInstanceAddress(instanceAddress);
        }
    }
}
