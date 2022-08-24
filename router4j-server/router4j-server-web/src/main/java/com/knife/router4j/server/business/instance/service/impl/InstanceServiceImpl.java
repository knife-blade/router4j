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

        List<String> allServices = applicationService.findAllServices();
        for (String serviceName : allServices) {
            List<InstanceInfo> instanceInfos =
                    applicationService.findInstance(serviceName);
            for (InstanceInfo instanceInfo : instanceInfos) {
                String instanceAddress = instanceInfo.instanceAddressWithoutProtocol();

                // 如果已存在，则不再添加
                boolean exist = false;
                for (InstanceVO instanceVO : instanceVOS) {
                    if (instanceVO.getServiceName().equals(serviceName)
                            && instanceVO.getInstanceAddress().equals(instanceAddress)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    InstanceVO instanceVO = new InstanceVO();
                    instanceVO.setServiceName(serviceName);
                    instanceVO.setInstanceAddress(instanceAddress);
                    instanceVOS.add(instanceVO);
                }
            }
        }

        fillIsDefaultInstance(instanceVOS);

        return instanceVOS;
    }

    /**
     * 填充是否是默认值字段
     *
     * @param instanceVOS 实例列表
     */
    private void fillIsDefaultInstance(List<InstanceVO> instanceVOS) {
        // 获取设置到Redis中的所有默认实例
        List<String> allDefaultInstances = defaultInstanceUtil.findAllDefaultInstance();

        for (InstanceVO instanceVO : instanceVOS) {
            if (allDefaultInstances.contains(instanceVO.getInstanceAddress())) {
                instanceVO.setIsDefaultInstance(true);
            } else {
                instanceVO.setIsDefaultInstance(false);
            }
        }
    }
}
