package com.knife.router4j.server.business.rule.service.impl;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.entity.RuleInfo;
import com.knife.router4j.common.util.ServerPathRuleUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.rule.service.RuleService;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {
    @Autowired
    private ServerPathRuleUtil serverPathRuleUtil;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public PageResponse<RuleInfo> page(PathRuleRequest pathRuleQueryRequest, PageRequest pageRequest) {
        List<RuleInfo> ruleInfoList = serverPathRuleUtil.findRule(pathRuleQueryRequest);

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

    @Override
    public List<String> findAllApplicationNames() {
        List<String> allApplicationNamesOfRedis = serverPathRuleUtil.findApplicationNames();
        List<String> allApplicationNamesOfRegistry = applicationService.findAllApplicationNames();

        List<String> result = new ArrayList<>(allApplicationNamesOfRedis);
        for (String applicationName : allApplicationNamesOfRegistry) {
            if (!result.contains(applicationName)) {
                result.add(applicationName);
            }
        }
        return result;
    }

    @Override
    public List<String> findInstanceAddresses(String applicationName) {
        List<String> instanceAddressesOfRedis =
                serverPathRuleUtil.findInstanceAddresses(applicationName);

        List<InstanceInfo> instanceInfoList = applicationService.findInstance(applicationName);
        List<String> instanceAddressesOfRegistry = instanceInfoList.stream()
                .map(InstanceInfo::instanceAddressWithoutProtocol)
                .collect(Collectors.toList());

        List<String> result = new ArrayList<>(instanceAddressesOfRedis);
        for (String instanceAddress : instanceAddressesOfRegistry) {
            if (!result.contains(instanceAddress)) {
                result.add(instanceAddress);
            }
        }

        return result;
    }
}
