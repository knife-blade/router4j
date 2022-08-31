package com.knife.router4j.common.util;

import com.knife.router4j.common.property.GuideProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;

public class GuideUtil {
    @Autowired
    private GuideProperties guideProperties;

    public void save(String text) {
        RBucket<String> bucket = RedissonHolder.getRedissonClient()
                .getBucket(guideProperties.getCacheKey());
        bucket.set(text);
    }

    public String find() {
        RBucket<String> bucket = RedissonHolder.getRedissonClient()
                .getBucket(guideProperties.getCacheKey());
        return bucket.get();
    }
}
