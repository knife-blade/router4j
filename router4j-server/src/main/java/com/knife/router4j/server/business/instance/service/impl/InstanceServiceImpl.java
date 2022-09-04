package com.knife.router4j.server.business.instance.service.impl;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.entity.DefaultInstanceInfo;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.instance.helper.InstanceHelper;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
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
    public List<String> findAllApplicationNames(String applicationName) {
        List<InstanceVO> defaultInstanceList = findDefaultInstance(applicationName);
        return defaultInstanceList.stream()
                .map(InstanceVO::getApplicationName)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<InstanceVO> findDefaultInstancePage(String applicationName,
                                                            PageRequest pageRequest) {
        List<InstanceVO> defaultInstanceList = findDefaultInstance(applicationName);
        return PageUtil.toPage(defaultInstanceList, pageRequest);
    }

    private List<InstanceVO> findDefaultInstance(String applicationName) {
        List<InstanceInfo> instanceInfos;
        if (StringUtils.hasText(applicationName)) {
            instanceInfos = findInstanceOfRegistry(applicationName);
        } else {
            instanceInfos = findAllInstanceOfRegistry();
        }

        List<InstanceVO> instanceVOS = InstanceHelper.toInstanceVO(instanceInfos);
        fillIsRunning(instanceVOS);

        List<InstanceVO> instanceVOListResult = findInstanceOfRedis(instanceVOS, applicationName);

        instanceVOListResult = instanceVOListResult.stream()
                .sorted(Comparator.comparing(InstanceVO::getInstanceAddress))
                .collect(Collectors.toList());
        return instanceVOListResult;
    }

    private List<InstanceInfo> findAllInstanceOfRegistry() {
        List<InstanceInfo> instanceInfoListResult = new ArrayList<>();

        List<String> allApplicationNames = applicationService.findAllApplicationNames();
        for (String applicationName : allApplicationNames) {
            List<InstanceInfo> instanceInfos = applicationService.findInstance(applicationName);
            instanceInfoListResult.addAll(instanceInfos);
        }

        return instanceInfoListResult;
    }

    private List<InstanceInfo> findInstanceOfRegistry(String applicationName) {
        return applicationService.findInstance(applicationName);
    }

    /**
     * 填充是否正在运行字段
     *
     * @param instanceVOS 实例列表
     */
    private void fillIsRunning(List<InstanceVO> instanceVOS) {
        for (InstanceVO instanceVO : instanceVOS) {
            instanceVO.setIsRunning(true);
        }
    }

    /**
     * 填充是否是默认值字段
     *
     * @param instanceVOS 实例列表
     * @return 填充好的实例列表
     */
    private List<InstanceVO> findInstanceOfRedis(List<InstanceVO> instanceVOS, String applicationName) {
        List<InstanceVO> instanceVOListResult = new ArrayList<>(instanceVOS);

        // 获取设置到Redis中的所有默认实例
        List<DefaultInstanceInfo> defaultInstanceListOfRedis =
                defaultInstanceUtil.findDefaultInstance(applicationName);

        for (DefaultInstanceInfo defaultInstanceOfRedis : defaultInstanceListOfRedis) {
            String applicationNameOfRedis = defaultInstanceOfRedis.getApplicationName();
            String instanceAddressOfRedis = defaultInstanceOfRedis.getInstanceAddress();
            Boolean isForceRoute = defaultInstanceOfRedis.getIsForceRoute();

            // 如果结果中已存在，则不再添加
            boolean exist = false;
            for (InstanceVO instanceVO : instanceVOListResult) {
                if (applicationNameOfRedis.equals(instanceVO.getApplicationName())
                        && instanceAddressOfRedis.equals(instanceVO.getInstanceAddress())) {
                    instanceVO.setIsDefaultInstance(true);
                    instanceVO.setIsForceRoute(isForceRoute);
                    exist = true;
                    break;
                }
            }
            // 如果不存在，则添加
            if (!exist) {
                InstanceVO instanceVO = new InstanceVO();
                instanceVO.setApplicationName(applicationNameOfRedis);
                instanceVO.setInstanceAddress(instanceAddressOfRedis);
                instanceVO.setIsDefaultInstance(true);
                instanceVO.setIsForceRoute(isForceRoute);
                instanceVO.setIsRunning(false);
                instanceVOListResult.add(instanceVO);
            }
        }
        return instanceVOListResult;
    }
}
