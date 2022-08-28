package com.knife.router4j.common.util;

import com.knife.router4j.common.entity.*;
import com.knife.router4j.common.helper.InstanceInfoHelper;
import com.knife.router4j.common.helper.RuleKeyHelper;
import com.knife.router4j.common.redis.RedissonHolder;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路径的规则
 */
public class PathRuleUtil {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 添加规则
     *
     * @param pathRuleRequest 路径规则请求体
     */
    public void addRule(PathRuleRequest pathRuleRequest) {
        RList<String> list = RedissonHolder.getRedissonClient().getList(
                RuleKeyHelper.assembleAddKey(pathRuleRequest));
        if (!list.contains(pathRuleRequest.getPathPattern())) {
            list.add(pathRuleRequest.getPathPattern());
        }
    }

    /**
     * 查询规则（用于前端查询）
     *
     * @param pathRuleRequest 路径规则请求体
     * @return 路径模板列表
     */
    public List<RuleInfo> findRule(PathRuleRequest pathRuleRequest) {
        Iterable<String> keysByPattern = RedissonHolder.getRedissonClient()
                .getKeys()
                .getKeysByPattern(RuleKeyHelper.assembleSearchKey(pathRuleRequest));

        List<RuleInfo> result = new ArrayList<>();
        for (String key : keysByPattern) {
            RList<String> pathPatternList = RedissonHolder.getRedissonClient().getList(key);
            String applicationName = RuleKeyHelper.parseApplicationName(key);
            String instanceAddress = RuleKeyHelper.parseInstanceAddress(key);
            for (String pathPattern : pathPatternList) {
                if (StringUtils.hasText(pathRuleRequest.getPathPattern())) {
                    if (pathMatcher.match(pathPattern, pathRuleRequest.getPathPattern())) {
                        RuleInfo ruleInfo = new RuleInfo();
                        ruleInfo.setApplicationName(applicationName);
                        ruleInfo.setInstanceAddress(instanceAddress);
                        ruleInfo.setPathPattern(pathPattern);
                        result.add(ruleInfo);
                    }
                } else {
                    RuleInfo ruleInfo = new RuleInfo();
                    ruleInfo.setApplicationName(applicationName);
                    ruleInfo.setInstanceAddress(instanceAddress);
                    ruleInfo.setPathPattern(pathPattern);
                    result.add(ruleInfo);
                }
            }
        }
        return result;
    }

    /**
     * 通过路径找到规则中的实例（用于网关和feign）
     *
     * @param path 路径。例如：/order/add
     * @return 实例地址。例如：127.0.0.1:8080
     */
    public InstanceInfo findMatchedInstance(String path) {
        return findMatchedInstance(null, path);
    }

    /**
     * 通过路径找到规则中的实例（用于网关和feign）
     *
     * @param applicationName 服务名
     * @param path        路径。例如：/order/add
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

        String prefix = RuleKeyHelper.assembleSearchKey(applicationName);

        // 模糊查找出所有实例的key
        Iterable<String> ruleKeys = keys.getKeysByPattern(prefix + "*");

        for (String ruleKey : ruleKeys) {
            // 取出每个实例的所有指定好的路径规则
            RList<String> pathPatterns = RedissonHolder.getRedissonClient().getList(ruleKey);

            // 找出每个实例里匹配的最长的pathPattern
            for (String pathPattern : pathPatterns) {
                if (pathMatcher.match(pathPattern, path)) {
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
            String instanceAddress = RuleKeyHelper.parseInstanceAddress(matchedKey);
            return InstanceInfoHelper.assembleInstanceAddress(instanceAddress);
        }
    }

    /**
     * 清除所有规则
     */
    public void deleteAllRule() {
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        keys.deleteByPattern(RuleKeyHelper.assembleSearchKey("*", "*"));
    }

    /**
     * 根据key删除规则
     *
     * @param key 要删除的数据的key
     */
    public void deleteRuleByKey(String key) {
        RBucket<String> bucket = RedissonHolder.getRedissonClient().getBucket(key);
        bucket.delete();
    }

    /**
     * 根据路径匹配删除规则
     *
     * @param pathRuleRequest 路径规则请求体
     */
    public void deleteRule(PathRuleRequest pathRuleRequest) {
        RList<String> list = RedissonHolder.getRedissonClient()
                .getList(RuleKeyHelper.assembleSearchKey(pathRuleRequest));
        list.remove(pathRuleRequest.getPathPattern());
    }

    /**
     * 清除某个服务的实例的所有规则
     *
     * @param applicationName     服务名字
     * @param instanceAddress 实例地址。例：127.0.0.1:8080
     */
    public void deleteRule(String applicationName, String instanceAddress) {
        String key = RuleKeyHelper.assembleSearchKey(applicationName, instanceAddress);
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        keys.deleteByPattern(key);
    }

    /**
     * 清除服务的所有规则
     *
     * @param applicationName 服务名字
     */
    public void deleteRuleByServiceName(String applicationName) {
        deleteRule(applicationName, "*");
    }

    /**
     * 清除实例的所有规则
     *
     * @param instanceAddress 实例地址。例：127.0.0.1:8080
     */
    public void deleteRuleByInstanceAddress(String instanceAddress) {
        deleteRule("*", instanceAddress);
    }
}
