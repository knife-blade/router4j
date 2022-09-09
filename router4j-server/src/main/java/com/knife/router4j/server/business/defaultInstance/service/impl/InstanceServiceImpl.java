package com.knife.router4j.server.business.defaultInstance.service.impl;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.entity.DefaultInstanceInfo;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.defaultInstance.helper.InstanceHelper;
import com.knife.router4j.server.business.defaultInstance.request.InstanceReq;
import com.knife.router4j.server.business.defaultInstance.service.InstanceService;
import com.knife.router4j.server.business.defaultInstance.vo.DefaultInstanceVO;
import com.knife.router4j.server.business.defaultInstance.vo.InstanceVO;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;
import com.knife.router4j.server.common.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    @Override
    public List<InstanceVO> findAllInstance(InstanceVO instanceVO) {
        List<DefaultInstanceVO> defaultInstanceList = findDefaultInstance(
                instanceVO.getApplicationName(),
                instanceVO.getInstanceIp(),
                instanceVO.getInstancePort());

        List<InstanceVO> instanceVOS = new ArrayList<>();
        for (DefaultInstanceVO defaultInstanceVO : defaultInstanceList) {
            InstanceVO tmpInstanceVO = new InstanceVO();
            tmpInstanceVO.setApplicationName(defaultInstanceVO.getApplicationName());

            String[] strings = defaultInstanceVO.getInstanceAddress().split(":");
            tmpInstanceVO.setInstanceIp(strings[0]);
            tmpInstanceVO.setInstancePort(Integer.parseInt(strings[1]));
            instanceVOS.add(tmpInstanceVO);
        }

        return instanceVOS;
    }

    @Override
    public PageResponse<DefaultInstanceVO> findDefaultInstancePage(InstanceVO instanceVO,
                                                                   PageRequest pageRequest) {
        List<DefaultInstanceVO> defaultInstanceList = findDefaultInstance(
                instanceVO.getApplicationName(),
                instanceVO.getInstanceIp(),
                instanceVO.getInstancePort());
        return PageUtil.toPage(defaultInstanceList, pageRequest);
    }

    @Override
    public void setupDefaultInstance(List<InstanceReq> instanceReqs) {
        for (InstanceReq instanceReq : instanceReqs) {
            String applicationName = instanceReq.getApplicationName();
            String instanceAddress = instanceReq.getInstanceAddress();

            if (instanceReq.getIsForceRoute() != null
                    && instanceReq.getIsForceRoute()) {
                defaultInstanceUtil.markAsDefaultInstance(
                        applicationName, instanceAddress, true);
            } else {
                if (instanceReq.getIsDefaultInstance() != null
                        && instanceReq.getIsDefaultInstance()) {
                    defaultInstanceUtil.markAsDefaultInstance(
                            applicationName, instanceAddress, false);
                } else {
                    defaultInstanceUtil.cancelDefaultInstance(applicationName, instanceAddress);
                }
            }
        }
    }

    private List<DefaultInstanceVO> findDefaultInstance(String applicationName,
                                                        String instanceIp,
                                                        Integer instancePort) {
        String instanceAddress = null;
        if (StringUtils.hasText(instanceIp) || instancePort != null) {
            instanceAddress = instanceIp + ":" + instancePort;
        }

        List<InstanceInfo> instanceInfos;
        if (StringUtils.hasText(applicationName)) {
            instanceInfos = findInstanceOfRegistry(applicationName);
        } else {
            instanceInfos = findAllInstanceOfRegistry();
        }

        List<DefaultInstanceVO> defaultInstanceVOS = InstanceHelper.toInstanceVO(instanceInfos);
        fillIsRunning(defaultInstanceVOS);

        List<DefaultInstanceVO> defaultInstanceVOListResult = findInstanceOfRedis(
                defaultInstanceVOS, applicationName, instanceAddress);

        defaultInstanceVOListResult = defaultInstanceVOListResult.stream()
                .sorted(Comparator.comparing(DefaultInstanceVO::getInstanceAddress))
                .collect(Collectors.toList());
        return defaultInstanceVOListResult;
    }

    /**
     * 查找注册中心里的所有服务
     */
    private List<InstanceInfo> findAllInstanceOfRegistry() {
        List<InstanceInfo> instanceInfoListResult = new ArrayList<>();

        List<String> allApplicationNames = applicationService.findAllApplicationNames();
        for (String applicationName : allApplicationNames) {
            List<InstanceInfo> instanceInfos = applicationService.findInstance(applicationName);
            instanceInfoListResult.addAll(instanceInfos);
        }

        return instanceInfoListResult;
    }

    /**
     * 查找注册中心里的实例
     */
    private List<InstanceInfo> findInstanceOfRegistry(String applicationName) {
        return applicationService.findInstance(applicationName);
    }

    /**
     * 填充是否正在运行字段
     *
     * @param defaultInstanceVOS 实例列表
     */
    private void fillIsRunning(List<DefaultInstanceVO> defaultInstanceVOS) {
        for (DefaultInstanceVO defaultInstanceVO : defaultInstanceVOS) {
            defaultInstanceVO.setIsRunning(true);
        }
    }

    /**
     * 填充是否是默认值字段
     *
     * @param defaultInstanceVOS 实例列表
     * @return 填充好的实例列表
     */
    private List<DefaultInstanceVO> findInstanceOfRedis(List<DefaultInstanceVO> defaultInstanceVOS,
                                                        String applicationName,
                                                        String instanceAddress) {
        List<DefaultInstanceVO> defaultInstanceVOListResult = new ArrayList<>(defaultInstanceVOS);

        // 获取设置到Redis中的所有默认实例
        List<DefaultInstanceInfo> defaultInstanceListOfRedis =
                defaultInstanceUtil.findDefaultInstance(applicationName, instanceAddress);

        for (DefaultInstanceInfo defaultInstanceOfRedis : defaultInstanceListOfRedis) {
            String applicationNameOfRedis = defaultInstanceOfRedis.getApplicationName();
            String instanceAddressOfRedis = defaultInstanceOfRedis.getInstanceAddress();
            Boolean isForceRoute = defaultInstanceOfRedis.getIsForceRoute();

            // 如果结果中已存在，则不再添加
            boolean exist = false;
            for (DefaultInstanceVO defaultInstanceVO : defaultInstanceVOListResult) {
                if (applicationNameOfRedis.equals(defaultInstanceVO.getApplicationName())
                        && instanceAddressOfRedis.equals(defaultInstanceVO.getInstanceAddress())) {
                    defaultInstanceVO.setIsDefaultInstance(true);
                    defaultInstanceVO.setIsForceRoute(isForceRoute);
                    exist = true;
                    break;
                }
            }
            // 如果不存在，则添加
            if (!exist) {
                DefaultInstanceVO defaultInstanceVO = new DefaultInstanceVO();
                defaultInstanceVO.setApplicationName(applicationNameOfRedis);
                defaultInstanceVO.setInstanceAddress(instanceAddressOfRedis);
                defaultInstanceVO.setIsDefaultInstance(true);
                defaultInstanceVO.setIsForceRoute(isForceRoute);
                defaultInstanceVO.setIsRunning(false);
                defaultInstanceVOListResult.add(defaultInstanceVO);
            }
        }
        return defaultInstanceVOListResult;
    }
}
