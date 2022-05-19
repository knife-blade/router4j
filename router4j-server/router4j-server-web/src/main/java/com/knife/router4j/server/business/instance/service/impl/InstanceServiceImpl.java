package com.knife.router4j.server.business.instance.service.impl;

import com.knife.router4j.common.entity.InstanceInfo;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    @Override
    public List<InstanceVO> findInstances() {
        List<InstanceVO> instanceVOS = new ArrayList<>();

        List<String> allDefaultInstance = defaultInstanceUtil.findAllDefaultInstance();

        List<String> allServices = applicationService.findAllServices();
        for (String serviceName : allServices) {
            List<InstanceInfo> instanceInfos =
                    applicationService.findInstance(serviceName);


        }
        return null;
    }
}
