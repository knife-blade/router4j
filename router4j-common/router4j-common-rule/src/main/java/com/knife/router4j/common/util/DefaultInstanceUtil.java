package com.knife.router4j.common.util;

import com.knife.router4j.common.constant.RedisConstant;
import com.knife.router4j.common.entity.RuleInfo;
import com.knife.router4j.common.helper.ParseRuleKeyHelper;
import com.knife.router4j.common.helper.RuleKeyHelper;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import com.knife.router4j.common.util.spring.ApplicationContextHolder;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;

import java.util.ArrayList;
import java.util.List;

public class DefaultInstanceUtil {

    private final RuleKeyHelper ruleKeyHelper;

    {
        String keyPrefix = ApplicationContextHolder.getContext()
                .getBean(RuleProperties.class)
                .getDefaultInstancePrefix();
        ruleKeyHelper = new RuleKeyHelper(keyPrefix);
    }

    /**
     * 标记为默认实例
     *
     * @param applicationName 应用名字
     * @param instanceAddress 实例地址
     */
    public void markAsDefaultInstance(String applicationName, String instanceAddress) {
        String key = ruleKeyHelper.assembleAddKey(applicationName, instanceAddress);
        RBucket<Boolean> bucket = RedissonHolder.getRedissonClient().getBucket(key);
        bucket.set(false);
    }

    /**
     * 取消默认实例
     *
     * @param applicationName 应用名字
     * @param instanceAddress 实例地址
     */
    public void cancelDefaultInstance(String applicationName, String instanceAddress) {
        String key = ruleKeyHelper.assembleDeleteKeyAccurate(applicationName, instanceAddress);
        RBucket<Boolean> bucket = RedissonHolder.getRedissonClient().getBucket(key);
        bucket.delete();
    }

    /**
     * 查找所有的默认实例
     *
     * @return 实例地址列表
     */
    public List<RuleInfo> findAllDefaultInstance() {
        String keyPattern = ruleKeyHelper.assembleSearchKey(RedisConstant.MATCH_ALL, RedisConstant.MATCH_ALL);
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        Iterable<String> keysByPattern = keys.getKeysByPattern(keyPattern);

        List<RuleInfo> ruleInfoList = new ArrayList<>();
        for (String key : keysByPattern) {
            String applicationName = ParseRuleKeyHelper.parseApplicationName(key);
            String instanceAddress = ParseRuleKeyHelper.parseInstanceAddress(key);
            RuleInfo ruleInfo = new RuleInfo();
            ruleInfo.setApplicationName(applicationName);
            ruleInfo.setInstanceAddress(instanceAddress);
            ruleInfoList.add(ruleInfo);
        }
        return ruleInfoList;
    }
}
