package com.knife.router4j.common.util;

import com.knife.router4j.common.constant.RedisConstant;
import com.knife.router4j.common.entity.DefaultInstanceInfo;
import com.knife.router4j.common.helper.ParseRuleKeyHelper;
import com.knife.router4j.common.helper.RuleKeyHelper;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultInstanceUtil {

    private RuleKeyHelper ruleKeyHelper;

    @Autowired
    public void setRuleProperties(RuleProperties ruleProperties) {
        String defaultInstancePrefix = ruleProperties.getDefaultInstancePrefix();
        ruleKeyHelper = new RuleKeyHelper(defaultInstancePrefix);
    }

    /**
     * 标记为默认实例
     *
     * @param applicationName 应用名字
     * @param instanceAddress 实例地址
     * @param isForceRoute    是否强制路由到本实例（无论是否有其他路由配置）
     */
    public void markAsDefaultInstance(String applicationName,
                                      String instanceAddress,
                                      Boolean isForceRoute) {
        checkDefaultInstanceExist(applicationName, instanceAddress);

        String key = ruleKeyHelper.assembleAddKey(applicationName, instanceAddress);
        RBucket<Boolean> bucket = RedissonHolder.getRedissonClient().getBucket(key);
        bucket.set(isForceRoute);
    }

    /**
     * 取消默认实例设置
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
     * 查找默认实例
     *
     * @return 实例地址列表
     */
    public List<DefaultInstanceInfo> findDefaultInstance(String applicationName,
                                                         String instanceAddress) {
        String keyPattern = ruleKeyHelper.assembleSearchKey(
                applicationName, instanceAddress);
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        Iterable<String> keysByPattern = keys.getKeysByPattern(keyPattern);

        List<DefaultInstanceInfo> defaultInstanceInfos = new ArrayList<>();
        for (String key : keysByPattern) {
            RBucket<Boolean> bucket = RedissonHolder.getRedissonClient().getBucket(key);
            Boolean isForceRoute = bucket.get();

            String tmpApplicationName = ParseRuleKeyHelper.parseApplicationName(key);
            String tmpInstanceAddress = ParseRuleKeyHelper.parseInstanceAddress(key);
            DefaultInstanceInfo defaultInstanceInfo = new DefaultInstanceInfo();
            defaultInstanceInfo.setApplicationName(tmpApplicationName);
            defaultInstanceInfo.setInstanceAddress(tmpInstanceAddress);
            defaultInstanceInfo.setIsForceRoute(isForceRoute);
            defaultInstanceInfos.add(defaultInstanceInfo);
        }
        return defaultInstanceInfos;
    }

    /**
     * 一个应用只能有一个默认实例
     *
     * @param applicationName 应用名字
     * @param instanceAddress 实例地址
     */
    private void checkDefaultInstanceExist(String applicationName,
                                           String instanceAddress) {
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        Iterable<String> keysByPattern =
                keys.getKeysByPattern(ruleKeyHelper.assembleSearchKey(applicationName));
        List<String> keyList = new ArrayList<>();

        for (String key : keysByPattern) {
            keyList.add(key);
        }

        if (keyList.size() == 0) {
            return;
        }

        // 如果有多个
        if (keyList.size() > 1) {
            List<String> instanceAddressList = keyList.stream()
                    .map(ParseRuleKeyHelper::parseInstanceAddress)
                    .collect(Collectors.toList());
            String errorMessage = String.format(
                    "当前应用（%s）设置了多个默认实例，请处理。设置的默认实例为：（%s）",
                    applicationName, instanceAddressList);
            throw new RuntimeException(errorMessage);
        }

        // 如果应用已经设置了其他默认实例，则报异常
        String key = keyList.get(0);
        if (!key.contains(instanceAddress)) {
            String errorMessage = String.format(
                    "当前应用（%s）已经设置了默认实例（%s）",
                    applicationName, ParseRuleKeyHelper.parseInstanceAddress(key));
            throw new RuntimeException(errorMessage);
        }
    }
}
