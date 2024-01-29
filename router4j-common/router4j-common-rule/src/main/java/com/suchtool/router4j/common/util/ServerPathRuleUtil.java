package com.suchtool.router4j.common.util;

import com.suchtool.router4j.common.constant.RedisConstant;
import com.suchtool.router4j.common.entity.PathRuleRequest;
import com.suchtool.router4j.common.entity.PathPatternInfo;
import com.suchtool.router4j.common.helper.ParseRuleKeyHelper;
import com.suchtool.router4j.common.helper.PathMatchHelper;
import com.suchtool.router4j.common.helper.RuleKeyHelper;
import com.suchtool.router4j.common.property.RuleProperties;
import com.suchtool.router4j.common.redis.RedissonHolder;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路径的规则
 * 服务端使用
 */
public class ServerPathRuleUtil {

    private RuleKeyHelper ruleKeyHelper;

    @Autowired
    public void setRuleProperties(RuleProperties ruleProperties) {
        String pathPatternPrefix = ruleProperties.getPathPatternPrefix();
        ruleKeyHelper = new RuleKeyHelper(pathPatternPrefix);
    }

    /**
     * 添加规则
     *
     * @param pathRuleRequest 路径规则请求体
     */
    public void addRule(PathRuleRequest pathRuleRequest) {
        RList<String> list = RedissonHolder.getRedissonClient().getList(
                ruleKeyHelper.assembleAddKey(
                        pathRuleRequest.getApplicationName(),
                        pathRuleRequest.getInstanceAddress()));

        if (!list.contains(pathRuleRequest.getPathPattern())) {
            list.add(pathRuleRequest.getPathPattern());
        }
    }

    /**
     * 查询规则（用于前端查询）
     *
     * @param pathRuleRequest 路径规则请求体
     * @return 路径模板列表
     */
    public List<PathPatternInfo> findRule(PathRuleRequest pathRuleRequest) {
        Iterable<String> keysByPattern = RedissonHolder.getRedissonClient()
                .getKeys()
                .getKeysByPattern(ruleKeyHelper.assembleSearchKey(
                        pathRuleRequest.getApplicationName(), pathRuleRequest.getInstanceAddress()));

        List<PathPatternInfo> result = new ArrayList<>();
        for (String key : keysByPattern) {
            RList<String> rPathPatternList = RedissonHolder.getRedissonClient().getList(key);
            String applicationName = ParseRuleKeyHelper.parseApplicationName(key);
            String instanceAddress = ParseRuleKeyHelper.parseInstanceAddress(key);
            for (String pathPattern : rPathPatternList) {
                if (StringUtils.hasText(pathRuleRequest.getPathPattern())) {
                    // 如果redis的路径ant匹配入参或者包含入参，则认为匹配
                    if (PathMatchHelper.matchForSetting(pathPattern, pathRuleRequest.getPathPattern())) {
                        PathPatternInfo pathPatternInfo = new PathPatternInfo();
                        pathPatternInfo.setApplicationName(applicationName);
                        pathPatternInfo.setInstanceAddress(instanceAddress);
                        pathPatternInfo.setPathPattern(pathPattern);
                        result.add(pathPatternInfo);
                    }
                } else {
                    PathPatternInfo pathPatternInfo = new PathPatternInfo();
                    pathPatternInfo.setApplicationName(applicationName);
                    pathPatternInfo.setInstanceAddress(instanceAddress);
                    pathPatternInfo.setPathPattern(pathPattern);
                    result.add(pathPatternInfo);
                }
            }
        }
        return result;
    }

    /**
     * 查找所有的应用名字
     *
     * @return 应用名字列表
     */
    public List<String> findApplicationNames() {
        Iterable<String> keysByPattern = RedissonHolder.getRedissonClient()
                .getKeys()
                .getKeysByPattern(ruleKeyHelper.assembleSearchKey("*"));

        List<String> result = new ArrayList<>();
        for (String key : keysByPattern) {
            String applicationName = ParseRuleKeyHelper.parseApplicationName(key);
            result.add(applicationName);
        }
        return result.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 查找实例地址
     * @param applicationName 应用名字
     * @return 实例地址列表
     */
    public List<String> findInstanceAddresses(String applicationName) {
        Iterable<String> keysByPattern = RedissonHolder.getRedissonClient()
                .getKeys()
                .getKeysByPattern(ruleKeyHelper.assembleSearchKey(applicationName));

        List<String> result = new ArrayList<>();
        for (String key : keysByPattern) {
            String instanceAddress = ParseRuleKeyHelper.parseInstanceAddress(key);
            result.add(instanceAddress);
        }
        return result;
    }

    /**
     * 清除所有规则
     */
    public void deleteAllRule() {
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        keys.deleteByPattern(ruleKeyHelper.assembleSearchKey("*", "*"));
    }

    /**
     * 删除规则（精准）
     *
     * @param pathRuleRequest 路径规则请求体
     */
    public void deleteRuleAccurate(PathRuleRequest pathRuleRequest) {
        RList<String> list = RedissonHolder.getRedissonClient().getList(
                ruleKeyHelper.assembleDeleteKeyAccurate(
                        pathRuleRequest.getApplicationName(),
                        pathRuleRequest.getInstanceAddress()));

        list.remove(pathRuleRequest.getPathPattern());
    }

    /**
     * 删除规则（模糊）
     *
     * @param pathRuleRequest 路径规则请求体
     */
    public void deleteRuleFuzzy(PathRuleRequest pathRuleRequest) {
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        String keyPattern = ruleKeyHelper.assembleSearchKey(
                pathRuleRequest.getApplicationName(),
                pathRuleRequest.getInstanceAddress());

        if (!StringUtils.hasText(pathRuleRequest.getPathPattern())) {
            keys.deleteByPattern(keyPattern);
        } else {
            Iterable<String> keysByPattern = keys.getKeysByPattern(keyPattern);
            for (String key : keysByPattern) {
                RList<String> rPathPatternList = RedissonHolder.getRedissonClient().getList(key);
                for (String pathPattern : rPathPatternList) {
                    // 如果redis的路径ant匹配入参或者包含入参，则认为匹配
                    if (PathMatchHelper.matchForSetting(pathPattern, pathRuleRequest.getPathPattern())) {
                        rPathPatternList.remove(pathPattern);
                    }
                }
            }
        }
    }

    /**
     * 清除某个服务的实例的所有规则
     *
     * @param applicationName 服务名字
     * @param instanceAddress 实例地址。例：127.0.0.1:8080
     */
    public void deleteRuleAccurate(String applicationName, String instanceAddress) {
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();
        String key = ruleKeyHelper.assembleDeleteKeyAccurate(applicationName, instanceAddress);
        keys.deleteByPattern(key);
    }

    /**
     * 清除服务的所有规则
     *
     * @param applicationName 服务名字
     */
    public void deleteRuleByServiceName(String applicationName) {
        deleteRuleAccurate(applicationName, "*");
    }

    /**
     * 清除实例的所有规则
     *
     * @param instanceAddress 实例地址。例：127.0.0.1:8080
     */
    public void deleteRuleByInstanceAddress(String instanceAddress) {
        deleteRuleAccurate("*", instanceAddress);
    }



    /**
     * 检查pathPattern是否已经存在
     * @param pathPattern 路径
     */
    private void checkPathPatternExist(String pathPattern) {
        RKeys keys = RedissonHolder.getRedissonClient().getKeys();

        String keyPattern = ruleKeyHelper.assembleSearchKey(RedisConstant.MATCH_ALL);

        // 模糊查找出所有实例的key
        Iterable<String> ruleKeys = keys.getKeysByPattern(keyPattern);

        for (String ruleKey : ruleKeys) {
            // 取出每个实例的所有指定好的路径规则
            RList<String> pathPatterns = RedissonHolder.getRedissonClient().getList(ruleKey);

            // 找出完全匹配的pathPattern
            for (String pathPatternOfRedis : pathPatterns) {
                if (pathPattern.equals(pathPatternOfRedis)) {
                    String applicationName = ParseRuleKeyHelper.parseApplicationName(ruleKey);
                    String instanceAddress = ParseRuleKeyHelper.parseInstanceAddress(ruleKey);
                    String errorMessage = String.format("此路径已经存在：" +
                            "应用名字为%s，实例地址为%s", applicationName, instanceAddress);
                    throw new RuntimeException(errorMessage);
                }
            }
        }
    }
}
