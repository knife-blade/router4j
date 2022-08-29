package com.knife.router4j.common.helper;

import org.springframework.util.AntPathMatcher;

/**
 * 路径匹配工具类
 */
public class PathMatchHelper {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 请求路由时的匹配（gateway和feign）
     * @param pathInRedis Redis中存放的path
     * @param pathParam   请求进来的path
     * @return 是否匹配
     */
    public static boolean matchForRoute(String pathInRedis, String pathParam) {
        return antPathMatcher.match(pathInRedis, pathParam);
    }

    /**
     * 设置时的匹配（router4j-server-web项目）
     * @param pathInRedis Redis中存放的path
     * @param pathParam   请求进来的path
     * @return 是否匹配
     */
    public static boolean matchForSetting(String pathInRedis, String pathParam) {
        // 如果redis的路径ant匹配入参或者包含入参，则认为匹配
        return antPathMatcher.match(pathInRedis, pathParam)
                || pathInRedis.contains(pathParam);
    }
}
