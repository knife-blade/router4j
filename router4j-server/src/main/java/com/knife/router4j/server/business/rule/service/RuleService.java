package com.knife.router4j.server.business.rule.service;

import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.entity.RuleInfo;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;

import java.util.List;

public interface RuleService {
    PageResponse<RuleInfo> page(PathRuleRequest pathRuleQueryRequest, PageRequest pageRequest);

    List<String> findAllApplicationNames();

    List<String> findInstanceAddresses(String applicationName);
}
