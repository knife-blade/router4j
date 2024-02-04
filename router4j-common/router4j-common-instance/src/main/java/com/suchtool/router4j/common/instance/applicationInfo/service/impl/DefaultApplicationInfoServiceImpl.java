package com.suchtool.router4j.common.instance.applicationInfo.service.impl;

import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import com.suchtool.router4j.common.common.util.PageUtil;
import com.suchtool.router4j.common.instance.applicationInfo.bo.ApplicationPageBO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.InstancePageBO;
import com.suchtool.router4j.common.instance.applicationInfo.service.ApplicationInfoService;
import com.suchtool.router4j.common.instance.applicationInfo.vo.ApplicationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultApplicationInfoServiceImpl implements ApplicationInfoService {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public Router4jPageVO<ApplicationVO> findAllApplications(ApplicationPageBO applicationPageBO) {
        List<String> services = discoveryClient.getServices();
        if (CollectionUtils.isEmpty(services)) {
            return null;
        }

        List<ApplicationVO> applicationVOList = new ArrayList<>();
        for (String service : services) {
            ApplicationVO applicationVO = new ApplicationVO();
            applicationVO.setApplicationName(service);
            applicationVOList.add(applicationVO);
        }
        return PageUtil.toPage(applicationVOList, new Router4jPageBO());
    }

    @Override
    public Router4jPageVO<InstanceInfo> findInstances(InstancePageBO instancePageBO) {
        List<ServiceInstance> serviceInstances = discoveryClient
                .getInstances(instancePageBO.getApplicationName());

        if (CollectionUtils.isEmpty(serviceInstances)) {
            return null;
        }

        List<InstanceInfo> instanceInfos = new ArrayList<>();
        for (ServiceInstance serviceInstance : serviceInstances) {
            InstanceInfo instanceInfo = new InstanceInfo();
            instanceInfo.setApplicationName(serviceInstance.getServiceId());
            instanceInfo.setHost(serviceInstance.getHost());
            instanceInfo.setPort(serviceInstance.getPort());
            instanceInfos.add(instanceInfo);
        }
        return PageUtil.toPage(instanceInfos, instancePageBO);
    }
}
