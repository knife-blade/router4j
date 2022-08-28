package com.knife.router4j.common.redis;

import com.knife.router4j.common.property.RedisProperties;
import com.knife.router4j.common.util.spring.ApplicationContextHolder;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonHolder {
    private static RedissonClient redissonClient = null;

    static {
        Config config = new Config();

        RedisProperties redisProperties = ApplicationContextHolder
                .getContext().getBean(RedisProperties.class);

        String address = "redis://" + redisProperties.getHost()
                + ":" + redisProperties.getPort();

        //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec

        //config.setCodec(new org.redisson.client.codec.StringCodec());
        config.useSingleServer()
                .setAddress(address)
                .setPassword(redisProperties.getPassword())
                .setTimeout((int) redisProperties.getTimeout().getSeconds() * 1000)
                .setDatabase(redisProperties.getDatabase());

        redissonClient = Redisson.create(config);
    }

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
