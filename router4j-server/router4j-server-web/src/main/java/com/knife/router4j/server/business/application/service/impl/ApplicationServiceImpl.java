package com.knife.router4j.server.business.application.service.impl;

import com.knife.router4j.common.entity.InstanceInfo;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private InstanceService instanceService;

    @Value("${spring.application.name}")
    private String thisApplicationName;

    @Override
    public List<String> getAllApplication() {
        List<String> allServices = instanceService.findAllServices();
        // allServices.remove(thisApplicationName);

        return allServices;
    }

    @Override
    public List<InstanceInfo> findInstance(String serviceName) {
        return instanceService.findInstances(serviceName);
    }
}
