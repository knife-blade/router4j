package com.knife.router4j.server.business.application.util;

import com.knife.router4j.server.business.application.vo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NacosUtil {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获得所有的服务
     * @return 所有的服务
     */
    public List<String> getServices() {
        return discoveryClient.getServices();
    }

    /**
     * 根据服务ID获得实例
     * @param serviceId 服务ID，一般是服务名字
     * @return 服务对应的所有实例
     */
    public List<Instance> getInstances(String serviceId) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);

        List<Instance> instances = new ArrayList<>();
        for (ServiceInstance serviceInstance : serviceInstances) {
            Instance instance = new Instance();
            instance.setServiceId(serviceInstance.getServiceId());
            instance.setHost(serviceInstance.getHost());
            instance.setPort(serviceInstance.getPort());
            instances.add(instance);
        }
        return instances;
    }

}
