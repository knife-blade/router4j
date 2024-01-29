package com.suchtool.router4j.server.business.rule.service;

import com.suchtool.router4j.common.entity.PathPatternInfo;
import com.suchtool.router4j.common.entity.PathRuleRequest;
import com.suchtool.router4j.server.common.entity.PageRequest;
import com.suchtool.router4j.server.common.entity.PageResponse;

import java.util.List;

public interface RuleService {
    PageResponse<PathPatternInfo> page(PathRuleRequest pathRuleQueryRequest, PageRequest pageRequest);

    List<String> findAllApplicationNames();

    List<String> findInstanceAddresses(String applicationName);
}
