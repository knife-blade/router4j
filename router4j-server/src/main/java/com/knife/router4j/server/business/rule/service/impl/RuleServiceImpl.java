package com.knife.router4j.server.business.rule.service.impl;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.entity.PathPatternInfo;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.util.ServerPathRuleUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.rule.service.RuleService;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;
import com.knife.router4j.server.common.util.BeanHelper;
import com.knife.router4j.server.common.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public PageResponse<PathPatternInfo> page(PathRuleRequest pathRuleQueryRequest, PageRequest pageRequest) {
        List<PathPatternInfo> ruleInfoList = serverPathRuleUtil.findRule(pathRuleQueryRequest);

        ruleInfoList = ruleInfoList.stream()
                .sorted(Comparator.comparing(PathPatternInfo::getPathPattern))
                .collect(Collectors.toList());

        return PageUtil.toPage(ruleInfoList, pageRequest);
    }

    @Override
    public List<String> findAllApplicationNames() {
        List<String> allApplicationNamesOfRedis = serverPathRuleUtil.findApplicationNames();
        List<String> allApplicationNamesOfRegistry = applicationService.findAllApplicationNames();

        for (String applicationName : allApplicationNamesOfRegistry) {
            if (!allApplicationNamesOfRedis.contains(applicationName)) {
                allApplicationNamesOfRedis.add(applicationName);
            }
        }

        return allApplicationNamesOfRedis;
    }

    @Override
    public List<String> findInstanceAddresses(String applicationName) {
        if (!StringUtils.hasText(applicationName)) {
            return new ArrayList<>();
        }

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
