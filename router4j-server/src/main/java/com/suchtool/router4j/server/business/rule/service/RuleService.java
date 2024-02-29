package com.suchtool.router4j.server.business.rule.service;

import com.suchtool.router4j.rule.entity.PathPatternInfo;
import com.suchtool.router4j.rule.entity.PathRuleRequest;
import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;

import java.util.List;

public interface RuleService {
    Router4jPageVO<PathPatternInfo> page(PathRuleRequest pathRuleQueryRequest, Router4jPageBO router4jPageBO);

    List<String> findAllApplicationNames();

    List<String> findInstanceAddresses(String applicationName);
}
