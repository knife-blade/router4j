package com.knife.router4j.server.service.impl;

import com.knife.router4j.common.entity.InstanceInfo;
import com.knife.router4j.server.service.InstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Nacos实现
 */
@Slf4j
public class NacosImpl implements InstanceService {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List<String> findAllServices() {
        return discoveryClient.getServices();
    }

    @Override
    public List<InstanceInfo> findInstances(String serviceId) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
        List<InstanceInfo> instanceInfos = new ArrayList<>();
        for (ServiceInstance serviceInstance : serviceInstances) {
            InstanceInfo instanceInfo = new InstanceInfo();
            instanceInfo.setServiceName(serviceInstance.getServiceId());
            instanceInfo.setHost(serviceInstance.getHost());
            instanceInfo.setPort(serviceInstance.getPort());
            instanceInfos.add(instanceInfo);
        }
        return instanceInfos;
    }
}
