package com.knife.router4j.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonUtil {
    private static class SingletonHolder {
        private static final RedissonUtil INSTANCE = new RedissonUtil();
    }

    private RedissonUtil() {
    }

    public static RedissonUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public RedissonClient redissonClient() {
        Config config = new Config();

        //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
        //config.setCodec(new org.redisson.client.codec.StringCodec());
        config.useSingleServer()
                .setAddress("redis://47.108.105.159:6379")
                .setPassword("cen66lyz")         //设置密码
                .setConnectionPoolSize(50)       //设置对于master节点的连接池中连接数最大为50
                .setIdleConnectionTimeout(10000) //若当前连接池里的连接数量超过了最小空闲连接数，而同时连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
                .setConnectTimeout(3000)         //与节点建立连接时的等待超时。时间单位是毫秒。
                .setTimeout(3000)                //等待节点回复命令的时间。该时间从命令发送成功时开始计时。
                .setDatabase(5);                 //使用第db5

        return Redisson.create(config);
    }
}
