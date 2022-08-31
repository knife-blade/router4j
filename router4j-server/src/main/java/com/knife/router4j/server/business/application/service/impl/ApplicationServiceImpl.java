package com.knife.router4j.server.business.application.service.impl;

import com.knife.router4j.common.entity.InstanceInfo;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.service.ApplicationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationInfoService applicationInfoService;

    @Override
    public List<String> findAllApplications() {
        return applicationInfoService.findAllApplications();
    }

    @Override
    public List<InstanceInfo> findInstance(String applicationName) {
        return applicationInfoService.findInstances(applicationName);
    }
}
