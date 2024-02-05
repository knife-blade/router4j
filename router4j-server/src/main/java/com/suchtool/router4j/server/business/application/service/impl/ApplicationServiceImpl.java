package com.suchtool.router4j.server.business.application.service.impl;

import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.ApplicationPageBO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.InstancePageBO;
import com.suchtool.router4j.common.instance.applicationInfo.service.ApplicationInfoService;
import com.suchtool.router4j.common.instance.applicationInfo.vo.ApplicationVO;
import com.suchtool.router4j.server.business.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationInfoService applicationInfoService;

    private final long pageSize = 10000;

    @Override
    public List<String> findAllApplicationNames(String namespaceName) {
        ApplicationPageBO applicationPageBO = new ApplicationPageBO();
        applicationPageBO.setNamespaceName(namespaceName);
        applicationPageBO.setCurrent(1);
        applicationPageBO.setSize(pageSize);

        Router4jPageVO<ApplicationVO> allApplications = applicationInfoService
                .findAllApplications(applicationPageBO);

        List<ApplicationVO> dataList = allApplications.getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }

        return dataList.stream()
                .map(ApplicationVO::getApplicationName)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstanceInfo> findInstance(String namespaceName, String applicationName) {
        InstancePageBO instancePageBO = new InstancePageBO();
        instancePageBO.setNamespaceName(namespaceName);
        instancePageBO.setApplicationName(applicationName);
        instancePageBO.setCurrent(1);
        instancePageBO.setSize(pageSize);

        Router4jPageVO<InstanceInfo> pageVO = applicationInfoService.findInstances(instancePageBO);
        return pageVO.getDataList();
    }
}
