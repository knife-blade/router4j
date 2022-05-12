package com.knife.router4j.common.util;

import com.knife.router4j.common.entity.InstanceAddress;
import com.knife.router4j.common.property.PrefixProperties;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路径的规则
 */
public class PathRule {
    @Autowired
    private RuleProperties rule;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 将实例和路径绑定
     *
     * @param instanceAddress 实例地址
     * @param pathPattern     路径匹配符。例如：/order/add；/order/**
     */
    public void bind(InstanceAddress instanceAddress, String pathPattern) {
        RList<String> list = RedissonHolder.getRedissonClient().getList(
                rule.getPrefix().getSetting() + instanceAddress.addressWithProtocol());
        if (!list.contains(pathPattern)) {
            list.add(pathPattern);
        }
    }

    /**
     * 将实例地址和路径解除绑定
     *
     * @param instanceAddress 实例地址
     * @param pathPattern     路径匹配符。例如：/order/add；/order/**
     */
    public void unbind(InstanceAddress instanceAddress, String pathPattern) {
        RList<String> list = RedissonHolder.getRedissonClient().getList(
                rule.getPrefix().getSetting() + instanceAddress.addressWithProtocol());
        list.remove(pathPattern);
    }

    /**
     * 获取实例地址已经绑定的规则
     *
     * @param instanceAddress 实例信息
     * @return 路径规则列表
     */
    public List<String> findPathPatterns(InstanceAddress instanceAddress) {
        return RedissonHolder.getRedissonClient().getList(
                rule.getPrefix().getSetting() + instanceAddress.addressWithProtocol());
    }

    /**
     * 通过路径找到规则中的实例
     *
     * @param path 路径。例如：/order/add
     * @return 实例地址。例如：127.0.0.1:8080
     */
    public InstanceAddress findMatchedInstanceAddress(String path) {
        // 如果没开启rule功能，则直接返回null
        if (!rule.getEnable()) {
            return null;
        }

        // key：实例地址  value：最具体的pattern
        Map<String, String> matchedMap = new HashMap<>();

        RKeys keys = RedissonHolder.getRedissonClient().getKeys();

        // 模糊查找出所有实例的key
        Iterable<String> hostAndPortS = keys.getKeysByPattern(rule.getPrefix().getSetting() + "*");

        for (String hostAndPort : hostAndPortS) {
            // 取出每个实例的所有指定好的路径规则
            RList<String> pathPatterns = RedissonHolder.getRedissonClient().getList(hostAndPort);
            for (String pathPattern : pathPatterns) {
                if (pathMatcher.match(pathPattern, path)) {
                    String patternOfMap = matchedMap.get(hostAndPort);
                    if (patternOfMap == null
                            || pathPattern.length() > patternOfMap.length()) {
                        matchedMap.put(hostAndPort, pathPattern);
                    }
                }
            }
        }

        //找出匹配的最长的实例
        String matchedInstanceAddress = null;
        String longestPath = "";

        for (Map.Entry<String, String> entry : matchedMap.entrySet()) {
            if (entry.getValue().length() > longestPath.length()) {
                longestPath = entry.getValue();
                matchedInstanceAddress = entry.getKey();
            }
        }

        if (matchedInstanceAddress == null) {
            return null;
        } else {
            return assembleInstanceAddress(matchedInstanceAddress);
        }
    }

    private InstanceAddress assembleInstanceAddress(String urlAddress) {
        URL url = null;
        try {
            url = new URL(urlAddress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InstanceAddress instanceAddress = new InstanceAddress();
        instanceAddress.setProtocol(url.getProtocol());
        instanceAddress.setHost(url.getHost());
        instanceAddress.setPort(url.getPort());
        return instanceAddress;
    }
}
