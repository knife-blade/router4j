package com.knife.router4j.common.util;

import com.knife.router4j.common.property.InstructionProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;

public class InstructionUtil {
    @Autowired
    private InstructionProperties instructionProperties;

    public void save(String text) {
        RBucket<String> bucket = RedissonHolder.getRedissonClient()
                .getBucket(instructionProperties.getCacheKey());
        bucket.set(text);
    }

    public String find() {
        RBucket<String> bucket = RedissonHolder.getRedissonClient()
                .getBucket(instructionProperties.getCacheKey());
        return bucket.get();
    }
}
