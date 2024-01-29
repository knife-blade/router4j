package com.suchtool.router4j.server.business.application.service;


import com.suchtool.router4j.common.common.entity.InstanceInfo;

import java.util.List;

public interface ApplicationService {
    List<String> findAllApplicationNames();

    List<InstanceInfo> findInstance(String applicationName);
}
