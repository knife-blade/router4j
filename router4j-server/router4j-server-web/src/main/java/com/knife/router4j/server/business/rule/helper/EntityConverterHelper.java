package com.knife.router4j.server.business.rule.helper;

import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.server.business.rule.request.RuleAddReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteReq;
import com.knife.router4j.server.business.rule.request.RuleEditReq;
import lombok.Data;

/**
 * 实体转换工具
 */
@Data
public class EntityConverterHelper {
    public static PathRuleRequest toPathRuleRequest(RuleAddReq addReq) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setServiceName(addReq.getServiceName());
        pathRuleRequest.setInstanceAddress(addReq.getInstanceAddress());
        pathRuleRequest.setPathPattern(addReq.getPathPattern());

        return pathRuleRequest;
    }

    public static PathRuleRequest toPathRuleRequest(RuleDeleteReq deleteReq) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setServiceName(deleteReq.getServiceName());
        pathRuleRequest.setInstanceAddress(deleteReq.getInstanceAddress());
        pathRuleRequest.setPathPattern(deleteReq.getPathPattern());

        return pathRuleRequest;
    }

    public static PathRuleRequest toPathRuleRequestDelete(RuleEditReq editReq) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setServiceName(editReq.getServiceName());
        pathRuleRequest.setInstanceAddress(editReq.getInstanceAddress());
        pathRuleRequest.setPathPattern(editReq.getOldPathPattern());

        return pathRuleRequest;
    }

    public static PathRuleRequest toPathRuleRequestAdd(RuleEditReq editReq) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setServiceName(editReq.getServiceName());
        pathRuleRequest.setInstanceAddress(editReq.getInstanceAddress());
        pathRuleRequest.setPathPattern(editReq.getPathPattern());

        return pathRuleRequest;
    }

    public static PathRuleRequest toPathRuleRequest(String serviceName,
                                                       String instanceAddress) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setServiceName(serviceName);
        pathRuleRequest.setInstanceAddress(instanceAddress);

        return pathRuleRequest;
    }
}
