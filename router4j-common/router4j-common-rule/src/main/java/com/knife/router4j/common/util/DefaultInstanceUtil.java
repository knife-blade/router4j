package com.knife.router4j.common.util;

import com.knife.router4j.common.common.util.JsonUtil;
import com.knife.router4j.common.constant.RedisConstant;
import com.knife.router4j.common.entity.DefaultInstanceInfo;
import com.knife.router4j.common.entity.PathPatternInfo;
import com.knife.router4j.common.helper.ParseRuleKeyHelper;
import com.knife.router4j.common.helper.RuleKeyHelper;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 查找所有的默认实例
     *
     * @return 实例地址列表
     */
    public List<DefaultInstanceInfo> findDefaultInstance(String applicationName) {
        String keyPattern = ruleKeyHelper.assembleSearchKey(
                applicationName, RedisConstant.MATCH_ALL);
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
        Map<String, String> map = new HashMap<>();

        for (String key : keysByPattern) {
            RBucket<String> bucket = RedissonHolder.getRedissonClient().getBucket(key);
            String instanceAddressOfRedis = bucket.get();
            // 如果应用已经设置了其他默认实例，则报异常
            if (!key.contains(instanceAddress)) {

                String errorMessage = String.format(
                        "当前应用（%s）已经设置了默认实例（%s）",
                        applicationName, instanceAddressOfRedis);
                throw new RuntimeException(errorMessage);
            }
            map.put(key, instanceAddressOfRedis);
        }

        // 如果有多个
        if (map.size() > 1) {
            String errorMessage = String.format(
                    "当前应用（%s）设置了多个默认实例，请处理。设置的默认实例为：（%s）",
                    applicationName, JsonUtil.toJson(map));
            throw new RuntimeException(errorMessage);
        }
    }
}
