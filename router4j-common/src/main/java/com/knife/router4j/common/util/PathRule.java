package com.knife.router4j.common.util;

import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 路径的规则
 */
public class PathRule {
    @Autowired
    private RedissonClient redissonClient;

    @Value("${router4j.rule.prefix:router4j:rule:prefix:")
    private String prefix;

    public void bind(String hostAndPort, String path) {
        RList<String> list = redissonClient.getList(prefix + hostAndPort);
        if (!list.contains(path)) {
            list.add(path);
        }
    }

    public void unbind(String hostAndPort, String path) {
        RList<Object> list = redissonClient.getList(prefix + hostAndPort);
        list.remove(path);
    }

    public String findMatchedHostAndPort(String path) {
        RKeys keys = redissonClient.getKeys();
        Iterable<String> keysByPattern = keys.getKeysByPattern(prefix + "*");
        for (String key : keysByPattern) {

        }

        String hostAndPort = "";

        return hostAndPort;
    }
}
