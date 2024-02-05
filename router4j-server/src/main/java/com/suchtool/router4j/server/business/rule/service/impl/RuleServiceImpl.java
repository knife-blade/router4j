package com.suchtool.router4j.server.business.rule.service.impl;

import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import com.suchtool.router4j.common.common.util.Router4jPageUtil;
import com.suchtool.router4j.common.entity.PathPatternInfo;
import com.suchtool.router4j.common.entity.PathRuleRequest;
import com.suchtool.router4j.common.util.ServerPathRuleUtil;
import com.suchtool.router4j.server.business.application.service.ApplicationService;
import com.suchtool.router4j.server.business.rule.service.RuleService;
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
    public Router4jPageVO<PathPatternInfo> page(PathRuleRequest pathRuleQueryRequest, Router4jPageBO router4jPageBO) {
        List<PathPatternInfo> ruleInfoList = serverPathRuleUtil.findRule(pathRuleQueryRequest);

        ruleInfoList = ruleInfoList.stream()
                .sorted(Comparator.comparing(PathPatternInfo::getPathPattern))
                .collect(Collectors.toList());

        return Router4jPageUtil.toPage(ruleInfoList, router4jPageBO);
    }

    @Override
    public List<String> findAllApplicationNames() {
        return serverPathRuleUtil.findApplicationNames();
        // List<String> allApplicationNamesOfRegistry = applicationService.findAllApplicationNames();
        //
        // for (String applicationName : allApplicationNamesOfRegistry) {
        //     if (!allApplicationNamesOfRedis.contains(applicationName)) {
        //         allApplicationNamesOfRedis.add(applicationName);
        //     }
        // }
        //
        // return allApplicationNamesOfRedis;
    }

    @Override
    public List<String> findInstanceAddresses(String applicationName) {
        if (!StringUtils.hasText(applicationName)) {
            return new ArrayList<>();
        }

        return serverPathRuleUtil.findInstanceAddresses(applicationName);

        // List<InstanceInfo> instanceInfoList = applicationService.findInstance(null, applicationName);
        // List<String> instanceAddressesOfRegistry = instanceInfoList.stream()
        //         .map(InstanceInfo::instanceAddressWithoutProtocol)
        //         .collect(Collectors.toList());
        //
        // List<String> result = new ArrayList<>(instanceAddressesOfRedis);
        // for (String instanceAddress : instanceAddressesOfRegistry) {
        //     if (!result.contains(instanceAddress)) {
        //         result.add(instanceAddress);
        //     }
        // }
        //
        // return result;
    }
}
