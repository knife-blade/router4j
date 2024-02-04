package com.suchtool.router4j.common.util;

import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.constant.RedisConstant;
import com.suchtool.router4j.common.entity.DefaultInstanceInfo;
import com.suchtool.router4j.common.helper.InstanceInfoHelper;
import com.suchtool.router4j.common.helper.ParseRuleKeyHelper;
import com.suchtool.router4j.common.helper.PathMatchHelper;
import com.suchtool.router4j.common.helper.RuleKeyHelper;
import com.suchtool.router4j.common.instance.applicationInfo.service.ApplicationInfoService;
import com.suchtool.router4j.common.property.RuleProperties;
import com.suchtool.router4j.common.redis.RedissonHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路径的规则
 * 客户端使用：gateway、feign
 */
@Slf4j
public class ClientPathRuleUtil {
    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    private RuleKeyHelper pathPatternKeyHelper;

    @Autowired
    private ApplicationInfoService applicationInfoService;

    @Autowired
    public void setRuleProperties(RuleProperties ruleProperties) {
        String pathPatternPrefix = ruleProperties.getPathPatternPrefix();
        this.pathPatternKeyHelper = new RuleKeyHelper(pathPatternPrefix);
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
     * @param applicationName 应用名
     * @param path            路径。例如：/order/add
     * @return 实例信息
     */
    public InstanceInfo findMatchedInstance(String applicationName, String path) {
        InstanceInfo instanceInfo = null;

        // 如果设置了强制路由，则使用设置的实例
        DefaultInstanceInfo defaultInstance = findDefaultInstance(applicationName);
        if (defaultInstance != null
                && defaultInstance.getIsForceRoute()) {
            instanceInfo = InstanceInfoHelper.assembleInstanceAddress(
                    defaultInstance.getInstanceAddress());
        }

        // 查找所有匹配的实例。key：实例地址  value：pathPattern
        Map<String, String> matchedMap =
                findMatchedInstanceOfPathPattern(applicationName, path);

        // 找出所有的pathPattern里最长的
        String matchedKey = null;
        String longestPath = "";

        for (Map.Entry<String, String> entry : matchedMap.entrySet()) {
            if (entry.getValue().length() > longestPath.length()) {
                longestPath = entry.getValue();
                matchedKey = entry.getKey();
            }
        }

        // 如果路径没有对应的实例
        if (matchedKey == null) {
            // 如果有默认实例，则使用默认实例
            if (defaultInstance != null) {
                instanceInfo = InstanceInfoHelper.assembleInstanceAddress(
                        defaultInstance.getInstanceAddress());
            }
        } else {
            String instanceAddress = ParseRuleKeyHelper.parseInstanceAddress(matchedKey);
            instanceInfo = InstanceInfoHelper.assembleInstanceAddress(instanceAddress);
        }

        return filterOnline(instanceInfo, applicationName);
    }

    /**
     * 找出所有匹配的路径
     *
     * @param applicationName 应用名
     * @param path            路径
     * @return key：实例地址  value：pathPattern
     */
    private Map<String, String> findMatchedInstanceOfPathPattern(String applicationName,
                                                                 String path) {
        Map<String, String> matchedMap = new HashMap<>();

        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        String keyPattern = pathPatternKeyHelper.assembleSearchKey(applicationName);

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

        return matchedMap;
    }

    private DefaultInstanceInfo findDefaultInstance(String applicationName) {
        List<DefaultInstanceInfo> defaultInstances =
                defaultInstanceUtil.findDefaultInstance(applicationName, RedisConstant.MATCH_ALL);

        if (defaultInstances.size() == 0) {
            return null;
        }

        if (defaultInstances.size() > 1) {
            List<String> instanceAddresses = defaultInstances.stream()
                    .map(DefaultInstanceInfo::getInstanceAddress)
                    .collect(Collectors.toList());
            log.warn("此应用（{}）有多个默认实例（{}）", applicationName, instanceAddresses);
        }

        return defaultInstances.get(0);
    }

    /**
     * 实例在线才返回，否则返回null
     */
    private InstanceInfo filterOnline(InstanceInfo instanceInfo, String applicationName) {
        if (instanceInfo == null) {
            return null;
        }

        List<InstanceInfo> instanceInfos =
                applicationInfoService.findInstances();
        for (InstanceInfo instanceOfRegistry : instanceInfos) {
            if (instanceOfRegistry.getHost().equals(instanceInfo.getHost())
                && instanceOfRegistry.getPort() == instanceInfo.getPort()) {
                return instanceInfo;
            }
        }

        return null;
    }
}
