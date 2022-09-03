package com.knife.router4j.server.business.instance.service.impl;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.entity.RuleInfo;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    @Override
    public List<InstanceVO> findDefaultInstance(String applicationName) {
        List<InstanceInfo> instanceInfos;
        if (StringUtils.hasText(applicationName)) {
            instanceInfos = findInstanceOfRegistry(applicationName);
        } else {
            instanceInfos =  findAllInstanceOfRegistry();
        }

        return fill(instanceInfos);
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

    private List<InstanceVO> fill(List<InstanceInfo> instanceInfos) {
        List<InstanceVO> instanceVOS = new ArrayList<>();
        for (InstanceInfo instanceInfo : instanceInfos) {
            String applicationName = instanceInfo.getApplicationName();
            String instanceAddress = instanceInfo.instanceAddressWithoutProtocol();

            InstanceVO instanceVO = new InstanceVO();
            instanceVO.setApplicationName(applicationName);
            instanceVO.setInstanceAddress(instanceAddress);
            instanceVOS.add(instanceVO);
        }

        fillIsRunning(instanceVOS);

        return fillIsDefaultInstance(instanceVOS);
    }

    /**
     * 填充是否正在运行字段
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
    private List<InstanceVO> fillIsDefaultInstance(List<InstanceVO> instanceVOS) {
        List<InstanceVO> instanceVOListResult = new ArrayList<>(instanceVOS);

        // 获取设置到Redis中的所有默认实例
        List<RuleInfo> defaultInstanceListOfRedis = defaultInstanceUtil.findAllDefaultInstance();

        for (RuleInfo defaultInstanceOfRedis : defaultInstanceListOfRedis) {
            String applicationNameOfRedis = defaultInstanceOfRedis.getApplicationName();
            String instanceAddressOfRedis = defaultInstanceOfRedis.getInstanceAddress();

            // 如果已存在，则不再添加
            boolean exist = false;
            for (InstanceVO instanceVO : instanceVOListResult) {
                if (applicationNameOfRedis.equals(instanceVO.getApplicationName())
                        && instanceAddressOfRedis.equals(instanceVO.getInstanceAddress())) {
                    instanceVO.setIsDefaultInstance(true);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                InstanceVO instanceVO = new InstanceVO();
                instanceVO.setApplicationName(applicationNameOfRedis);
                instanceVO.setInstanceAddress(instanceAddressOfRedis);
                instanceVO.setIsDefaultInstance(true);
                instanceVO.setIsRunning(false);
                instanceVOListResult.add(instanceVO);
            }
        }
        return instanceVOListResult;
    }
}
