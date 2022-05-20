package com.knife.router4j.server.business.instance.service.impl;

import com.knife.router4j.common.entity.InstanceInfo;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    @Override
    public List<InstanceVO> findInstances() {
        List<InstanceVO> instanceVOS = new ArrayList<>();

        // 获取设置到Redis中的所有默认实例
        List<String> allDefaultInstances = defaultInstanceUtil.findAllDefaultInstance();

        // 获取所有服务中的所有实例（去重）
        List<String> allInstances = new ArrayList<>();
        List<String> allServices = applicationService.findAllServices();
        for (String serviceName : allServices) {
            List<InstanceInfo> instanceInfos =
                    applicationService.findInstance(serviceName);
            for (InstanceInfo instanceInfo : instanceInfos) {
                String instanceAddress = instanceInfo.instanceAddressWithoutProtocol();
                if (!allInstances.contains(instanceAddress)) {
                    allInstances.add(instanceAddress);
                }
            }
        }

        // 构造返回值
        for (String instance : allInstances) {
            InstanceVO instanceVO = new InstanceVO();
            instanceVO.setInstanceAddress(instance);
            if (allDefaultInstances.contains(instance)) {
                instanceVO.setIsDefaultInstance(true);
            } else {
                instanceVO.setIsDefaultInstance(false);
            }
        }

        return instanceVOS;
    }
}
