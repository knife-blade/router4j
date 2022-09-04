package com.knife.router4j.server.business.instance.helper;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.server.business.instance.vo.InstanceVO;

import java.util.ArrayList;
import java.util.List;

public class InstanceHelper {
    public static List<InstanceVO> toInstanceVO(List<InstanceInfo> instanceInfos) {
        List<InstanceVO> instanceVOS = new ArrayList<>();
        for (InstanceInfo instanceInfo : instanceInfos) {
            String applicationName = instanceInfo.getApplicationName();
            String instanceAddress = instanceInfo.instanceAddressWithoutProtocol();

            InstanceVO instanceVO = new InstanceVO();
            instanceVO.setApplicationName(applicationName);
            instanceVO.setInstanceAddress(instanceAddress);
            instanceVOS.add(instanceVO);
        }
        return instanceVOS;
    }
}
