package com.knife.router4j.server.business.application.service;

import com.knife.router4j.common.entity.InstanceInfo;

import java.util.List;

public interface ApplicationService {
    List<String> getAllApplication();

    List<InstanceInfo> findInstance(String serviceName);
}
