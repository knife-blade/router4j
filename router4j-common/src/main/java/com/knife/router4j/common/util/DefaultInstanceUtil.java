package com.knife.router4j.common.util;

import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DefaultInstanceUtil {
    @Autowired
    private RuleProperties rule;

    /**
     * 标记为默认实例
     * @param instanceAddresses 实例地址
     */
    public void markAsDefaultInstance(List<String> instanceAddresses) {
        RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix())
                .addAll(instanceAddresses);
    }

    /**
     * 取消默认实例
     * @param instanceAddresses 实例地址
     */
    public void cancelDefaultInstance(List<String> instanceAddresses) {
        RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix())
                .removeAll(instanceAddresses);
    }

    /**
     * 查找所有的默认实例
     * @return 实例地址列表
     */
    public List<String> findAllDefaultInstance() {
        return RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix());
    }
}
