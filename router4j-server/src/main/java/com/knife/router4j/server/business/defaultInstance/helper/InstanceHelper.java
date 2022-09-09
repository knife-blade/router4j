package com.knife.router4j.server.business.defaultInstance.helper;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.server.business.defaultInstance.vo.DefaultInstanceVO;

import java.util.ArrayList;
import java.util.List;

public class InstanceHelper {
    public static List<DefaultInstanceVO> toInstanceVO(List<InstanceInfo> instanceInfos) {
        List<DefaultInstanceVO> defaultInstanceVOS = new ArrayList<>();
        for (InstanceInfo instanceInfo : instanceInfos) {
            String applicationName = instanceInfo.getApplicationName();
            String instanceAddress = instanceInfo.instanceAddressWithoutProtocol();

            DefaultInstanceVO defaultInstanceVO = new DefaultInstanceVO();
            defaultInstanceVO.setApplicationName(applicationName);
            defaultInstanceVO.setInstanceAddress(instanceAddress);
            defaultInstanceVOS.add(defaultInstanceVO);
        }
        return defaultInstanceVOS;
    }
}
