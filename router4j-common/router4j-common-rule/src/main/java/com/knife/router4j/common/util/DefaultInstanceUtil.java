package com.knife.router4j.common.util;

import com.knife.router4j.common.constant.PathPatternConstant;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DefaultInstanceUtil {
    @Autowired
    private RuleProperties rule;

    @Autowired
    private ServerPathRuleUtil serverPathRuleUtil;

    /**
     * 标记为默认实例
     *
     * @param instanceAddresses 实例地址列表
     */
    public void markAsDefaultInstance(List<String> instanceAddresses) {
        RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix())
                .addAll(instanceAddresses);

        for (String instanceAddress : instanceAddresses) {
            PathRuleRequest pathRuleRequest = new PathRuleRequest();
            pathRuleRequest.setApplicationName("*");
            pathRuleRequest.setInstanceAddress(instanceAddress);
            pathRuleRequest.setPathPattern(PathPatternConstant.MATCH_ALL);
            serverPathRuleUtil.addRule(pathRuleRequest);
        }
    }

    /**
     * 取消默认实例
     *
     * @param instanceAddresses 实例地址列表
     */
    public void cancelDefaultInstance(List<String> instanceAddresses) {

        RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix())
                .removeAll(instanceAddresses);

        for (String instanceAddress : instanceAddresses) {
            PathRuleRequest pathRuleQueryRequest = new PathRuleRequest();
            pathRuleQueryRequest.setApplicationName("*");
            pathRuleQueryRequest.setInstanceAddress(instanceAddress);
            pathRuleQueryRequest.setPathPattern(PathPatternConstant.MATCH_ALL);
            serverPathRuleUtil.deleteRuleAccurate(pathRuleQueryRequest);
        }
    }

    /**
     * 查找所有的默认实例
     *
     * @return 实例地址列表
     */
    public List<String> findAllDefaultInstance() {
        return RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix());
    }
}
