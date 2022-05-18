package com.knife.router4j.common.util;

import com.knife.router4j.common.constant.PathPatternConstant;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.property.RuleProperties;
import com.knife.router4j.common.redis.RedissonHolder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultInstanceUtil {
    @Autowired
    private RuleProperties rule;

    @Autowired
    private PathRuleUtil pathRuleUtil;

    /**
     * 标记为默认实例
     *
     * @param pathRuleRequests 路径规则请求列表
     */
    public void markAsDefaultInstance(List<PathRuleRequest> pathRuleRequests) {
        List<String> instanceAddresses = pathRuleRequests.stream()
                .map(PathRuleRequest::getInstanceAddress)
                .distinct()
                .collect(Collectors.toList());

        RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix())
                .addAll(instanceAddresses);

        for (PathRuleRequest pathRuleRequest : pathRuleRequests) {
            pathRuleRequest.setPathPattern(PathPatternConstant.DEFAULT_PATTERN);
            pathRuleUtil.bind(pathRuleRequest);
        }
    }

    /**
     * 取消默认实例
     *
     * @param pathRuleRequests 路径规则请求列表
     */
    public void cancelDefaultInstance(List<PathRuleRequest> pathRuleRequests) {
        List<String> instanceAddresses = pathRuleRequests.stream()
                .map(PathRuleRequest::getInstanceAddress)
                .distinct()
                .collect(Collectors.toList());

        RedissonHolder.getRedissonClient()
                .getList(rule.getDefaultInstancePrefix())
                .removeAll(instanceAddresses);

        for (PathRuleRequest pathRuleRequest : pathRuleRequests) {
            pathRuleRequest.setPathPattern(PathPatternConstant.DEFAULT_PATTERN);
            pathRuleUtil.unbind(pathRuleRequest);
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
