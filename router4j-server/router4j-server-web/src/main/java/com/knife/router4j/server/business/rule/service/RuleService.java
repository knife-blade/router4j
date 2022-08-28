package com.knife.router4j.server.business.rule.service;

import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.entity.RuleInfo;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;

public interface RuleService {
    PageResponse<RuleInfo> find(PathRuleRequest pathRuleRequest, PageRequest pageRequest);
}
