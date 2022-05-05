package com.knife.router4j.common.util;

import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * 路径的规则
 */
public class PathRule {
    @Autowired
    private RedissonClient redissonClient;

    @Value("${router4j.rule.prefix:router4j:rule:prefix:")
    private String prefix;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 将域名+端口号和路径绑定
     * @param hostAndPort 域名+端口号。例如：127.0.0.1:8080
     * @param pathPattern 路径匹配符。例如：/order/add；/order/**
     */
    public void bind(String hostAndPort, String pathPattern) {
        RList<String> list = redissonClient.getList(prefix + hostAndPort);
        if (!list.contains(pathPattern)) {
            list.add(pathPattern);
        }
    }

    /**
     * 将域名+端口号和路径解除绑定
     * @param hostAndPort 域名+端口号。例如：127.0.0.1:8080
     * @param pathPattern 路径匹配符。例如：/order/add；/order/**
     */
    public void unbind(String hostAndPort, String pathPattern) {
        RList<String> list = redissonClient.getList(prefix + hostAndPort);
        list.remove(pathPattern);
    }

    /**
     * 通过路径找到规则中的端口号和域名
     * @param path 路径。例如：/order/add
     * @return 域名+端口号。例如：127.0.0.1:8080
     */
    public String findMatchedHostAndPort(String path) {
        // key：host+port  value：最具体的pattern
        Map<String, String> matchedMap = new HashMap<>();

        RKeys keys = redissonClient.getKeys();

        // 模糊查找出所有host+port的key
        Iterable<String> hostAndPortS = keys.getKeysByPattern(prefix + "*");

        for (String hostAndPort : hostAndPortS) {

            // 取出每个host+port的所有指定好的路径规则
            RList<String> pathPatterns = redissonClient.getList(hostAndPort);
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

        String matchedHostAndPort = null;
        String longestPath = "";

        for (Map.Entry<String, String> entry : matchedMap.entrySet()) {
            if (entry.getValue().length() > longestPath.length()) {
                longestPath = entry.getValue();
                matchedHostAndPort = entry.getKey();
            }
        }

        return matchedHostAndPort;
    }
}
