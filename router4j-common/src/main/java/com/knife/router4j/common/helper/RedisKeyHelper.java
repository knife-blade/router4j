package com.knife.router4j.common.helper;

import com.knife.router4j.common.constant.RedisConstant;

/**
 * Redis的key工具类
 */
public class RedisKeyHelper {

    /**
     * 组装key
     *
     * @param prefix       前缀
     * @param pathPattern  路径（ant格式）
     * @return 组装好的key
     */
    public static String assembleKey(String prefix, String pathPattern) {
        return prefix + RedisConstant.SEPARATOR + pathPattern;
    }

}
