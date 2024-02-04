package com.suchtool.router4j.common.instance.applicationInfo.service.impl;

import com.suchtool.nacosopenapi.api.NacosOpenApiUtil;
import com.suchtool.nacosopenapi.api.bo.NacosInstancePageBO;
import com.suchtool.nacosopenapi.api.bo.NacosServicePageBO;
import com.suchtool.nacosopenapi.api.vo.NacosInstanceVO;
import com.suchtool.nacosopenapi.api.vo.NacosNamespaceVO;
import com.suchtool.nacosopenapi.api.vo.NacosServiceVO;
import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import com.suchtool.router4j.common.common.util.PageUtil;
import com.suchtool.router4j.common.instance.applicationInfo.bo.ApplicationPageBO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.InstancePageBO;
import com.suchtool.router4j.common.instance.applicationInfo.service.ApplicationInfoService;
import com.suchtool.router4j.common.instance.applicationInfo.vo.ApplicationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NacosApplicationInfoServiceImpl implements ApplicationInfoService {
    @Autowired
    private NacosOpenApiUtil nacosOpenApiUtil;

    @Override
    public Boolean namespaceExist() {
        return true;
    }

    @Override
    public List<String> findAllNameSpaces() {
        List<NacosNamespaceVO> nacosNamespaceVOS = nacosOpenApiUtil.queryAllNamespace();
        if (CollectionUtils.isEmpty(nacosNamespaceVOS)) {
            return null;
        }

        return nacosNamespaceVOS.stream()
                .map(NacosNamespaceVO::getNamespaceShowName)
                .collect(Collectors.toList());
    }

    @Override
    public Router4jPageVO<ApplicationVO> findAllApplications(ApplicationPageBO applicationPageBO) {
        NacosServicePageBO nacosServicePageBO = new NacosServicePageBO();
        nacosServicePageBO.setNamespaceId(applicationPageBO.getNamespaceName());
        nacosServicePageBO.setPageNo((int) applicationPageBO.getCurrent());
        nacosServicePageBO.setPageSize((int) applicationPageBO.getSize());

        List<NacosServiceVO> serviceVOS = nacosOpenApiUtil.queryService(nacosServicePageBO);
        if (CollectionUtils.isEmpty(serviceVOS)) {
            return null;
        }

        List<ApplicationVO> applicationVOList = new ArrayList<>();
        for (NacosServiceVO serviceVO : serviceVOS) {
            ApplicationVO applicationVO = new ApplicationVO();
            applicationVO.setApplicationName(serviceVO.getName());
            applicationVOList.add(applicationVO);
        }
        return PageUtil.toPage(applicationVOList, applicationPageBO);
    }

    @Override
    public Router4jPageVO<InstanceInfo> findInstances(InstancePageBO instancePageBO) {
        NacosInstancePageBO nacosInstancePageBO = new NacosInstancePageBO();
        nacosInstancePageBO.setServiceName(instancePageBO.getApplicationName());
        nacosInstancePageBO.setNamespaceId(instancePageBO.getNamespaceName());
        nacosInstancePageBO.setPageNo((int) instancePageBO.getCurrent());
        nacosInstancePageBO.setPageSize((int) instancePageBO.getSize());

        List<NacosInstanceVO> nacosInstanceVOS = nacosOpenApiUtil.queryInstance(nacosInstancePageBO);
        if (CollectionUtils.isEmpty(nacosInstanceVOS)) {
            return null;
        }

        List<InstanceInfo> instanceInfos = new ArrayList<>();
        for (NacosInstanceVO nacosInstanceVO : nacosInstanceVOS) {
            InstanceInfo instanceInfo = new InstanceInfo();
            instanceInfo.setApplicationName(nacosInstanceVO.getInstanceId());
            instanceInfo.setHost(nacosInstanceVO.getIp());
            instanceInfo.setPort(nacosInstanceVO.getPort());
            instanceInfos.add(instanceInfo);
        }
        return PageUtil.toPage(instanceInfos, instancePageBO);
    }
}
