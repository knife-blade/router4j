package com.knife.router4j.common.util;

import com.knife.router4j.common.entity.InstanceInfo;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.helper.InstanceInfoHelper;
import com.knife.router4j.common.helper.RedisKeyHelper;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路径的规则
 */
public class PathRuleUtil {
    @Autowired
    private RuleProperties rule;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 将实例和路径绑定
     *
     * @param pathRuleRequest 路径规则请求体
     */
    public void bind(PathRuleRequest pathRuleRequest) {
        RList<String> list = RedissonHolder.getRedissonClient().getList(
                RedisKeyHelper.assembleKey(rule.getPathPatternPrefix(), pathRuleRequest));
        if (!list.contains(pathRuleRequest.getPathPattern())) {
            list.add(pathRuleRequest.getPathPattern());
        }
    }

    /**
     * 将实例地址和路径解除绑定
     *
     * @param pathRuleRequest 路径规则请求体
     */
    public void unbind(PathRuleRequest pathRuleRequest) {
        RList<String> list = RedissonHolder.getRedissonClient().getList(
                RedisKeyHelper.assembleKey(rule.getPathPatternPrefix(), pathRuleRequest));
        list.remove(pathRuleRequest.getPathPattern());
    }

    /**
     * 获取实例地址已经绑定的规则
     *
     * @param pathRuleRequest 路径规则请求体
     * @return 路径模板列表
     */
    public List<String> findPathPatterns(PathRuleRequest pathRuleRequest) {
        return RedissonHolder.getRedissonClient().getList(
                RedisKeyHelper.assembleKey(rule.getPathPatternPrefix(), pathRuleRequest));
    }

    /**
     * 通过路径找到规则中的实例
     *
     * @param path 路径。例如：/order/add
     * @return 实例地址。例如：127.0.0.1:8080
     */
    public InstanceInfo findMatchedInstance(String path) {
        return findMatchedInstance(null, path);
    }

    /**
     * 通过路径找到规则中的实例
     *
     * @param serviceName 服务名
     * @param path 路径。例如：/order/add
     * @return 实例信息
     */
    public InstanceInfo findMatchedInstance(String serviceName, String path) {
        // 如果没开启rule功能，则直接返回null
        if (!rule.getEnable()) {
            return null;
        }

        // key：实例地址  value：最具体的pattern
        Map<String, String> matchedMap = new HashMap<>();

        RKeys keys = RedissonHolder.getRedissonClient().getKeys();

        String prefix = RedisKeyHelper.assembleKey(rule.getPathPatternPrefix(), serviceName);

        // 模糊查找出所有实例的key
        Iterable<String> ruleKeys = keys.getKeysByPattern(prefix + "*");

        for (String ruleKey : ruleKeys) {
            // 取出每个实例的所有指定好的路径规则
            RList<String> pathPatterns = RedissonHolder.getRedissonClient()
                    .getList(ruleKey);

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
            String instanceAddress = RedisKeyHelper.parseInstanceAddress(matchedKey);
            return InstanceInfoHelper.assembleInstanceAddress(instanceAddress);
        }
    }

    /**
     * 清除所有规则
     */
    public void clearRuleAll() {
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        keys.deleteByPattern(rule.getPathPatternPrefix() + "*");
    }

    /**
     * 清除服务的所有规则
     * @param serviceName 服务名字
     */
    public void clearRule(String serviceName) {
        clearRule(serviceName, "*");
    }

    /**
     * 清除实例的所有规则
     * @param serviceName 服务名字
     * @param instanceAddress 实例地址
     */
    public void clearRule(String serviceName, String instanceAddress) {
        String prefix = RedisKeyHelper.assembleKey(
                rule.getPathPatternPrefix(), serviceName, instanceAddress);

        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        keys.deleteByPattern(prefix + "*");
    }
}
