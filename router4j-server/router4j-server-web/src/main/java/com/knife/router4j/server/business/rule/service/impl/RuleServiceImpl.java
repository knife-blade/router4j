package com.knife.router4j.server.business.rule.service.impl;

import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.entity.RuleInfo;
import com.knife.router4j.common.util.PathRuleUtil;
import com.knife.router4j.server.business.rule.service.RuleService;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {
    @Autowired
    private PathRuleUtil pathRuleUtil;

    @Override
    public PageResponse<RuleInfo> find(PathRuleRequest pathRuleQueryRequest, PageRequest pageRequest) {
        List<RuleInfo> ruleInfoList = pathRuleUtil.findRule(pathRuleQueryRequest);

        ruleInfoList = ruleInfoList.stream()
                .sorted(Comparator.comparing(RuleInfo::getPathPattern))
                .collect(Collectors.toList());

        int startIndex = (int) (pageRequest.getCurrent() * pageRequest.getSize());
        int allSize = ruleInfoList.size();
        int dataSize = allSize - startIndex;
        List<RuleInfo> ruleInfoListResult = ruleInfoList.subList(startIndex, dataSize - startIndex);

        PageResponse<RuleInfo> pageResponse = new PageResponse<>();
        pageResponse.setSize(dataSize);
        pageResponse.setCurrent(pageRequest.getCurrent());
        pageResponse.setTotal(allSize);
        pageResponse.setData(ruleInfoListResult);

        return pageResponse;
    }
}
