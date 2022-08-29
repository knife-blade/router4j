package com.knife.router4j.server.business.rule.helper;

import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.server.business.rule.request.RuleAddReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteAccurateReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteFuzzyReq;
import com.knife.router4j.server.business.rule.request.RuleEditReq;
import lombok.Data;

/**
 * 实体转换工具
 */
@Data
public class EntityConverterHelper {
    public static PathRuleRequest toPathRuleRequest(RuleAddReq addReq) {
        PathRuleRequest pathRuleAddRequest = new PathRuleRequest();
        pathRuleAddRequest.setApplicationName(addReq.getApplicationName());
        pathRuleAddRequest.setInstanceAddress(addReq.getInstanceAddress());
        pathRuleAddRequest.setPathPattern(addReq.getPathPattern());

        return pathRuleAddRequest;
    }

    public static PathRuleRequest toPathRuleRequest(RuleDeleteAccurateReq deleteReq) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setApplicationName(deleteReq.getApplicationName());
        pathRuleRequest.setInstanceAddress(deleteReq.getInstanceAddress());
        pathRuleRequest.setPathPattern(deleteReq.getPathPattern());

        return pathRuleRequest;
    }

    public static PathRuleRequest toPathRuleRequest(RuleDeleteFuzzyReq deleteReq) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setApplicationName(deleteReq.getApplicationName());
        pathRuleRequest.setInstanceAddress(deleteReq.getInstanceAddress());
        pathRuleRequest.setPathPattern(deleteReq.getPathPattern());

        return pathRuleRequest;
    }

    public static PathRuleRequest toPathRuleRequestDelete(RuleEditReq editReq) {
        PathRuleRequest pathRuleQueryRequest = new PathRuleRequest();
        pathRuleQueryRequest.setApplicationName(editReq.getApplicationName());
        pathRuleQueryRequest.setInstanceAddress(editReq.getOldInstanceAddress());
        pathRuleQueryRequest.setPathPattern(editReq.getOldPathPattern());

        return pathRuleQueryRequest;
    }

    public static PathRuleRequest toPathRuleRequestAdd(RuleEditReq editReq) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setApplicationName(editReq.getApplicationName());
        pathRuleRequest.setInstanceAddress(editReq.getNewInstanceAddress());
        pathRuleRequest.setPathPattern(editReq.getNewPathPattern());

        return pathRuleRequest;
    }

    public static PathRuleRequest toPathRuleRequest(String applicationName,
                                                    String instanceAddress) {
        PathRuleRequest pathRuleRequest = new PathRuleRequest();
        pathRuleRequest.setApplicationName(applicationName);
        pathRuleRequest.setInstanceAddress(instanceAddress);

        return pathRuleRequest;
    }
}
