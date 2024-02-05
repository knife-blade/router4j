package com.suchtool.router4j.server.business.instance.helper;

import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.suchtool.router4j.server.business.instance.vo.InstanceForHeaderVO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstanceHelper {
    public static List<DefaultInstanceVO> toDefaultInstanceVO(List<InstanceInfo> instanceInfos) {
        List<DefaultInstanceVO> defaultInstanceVOS = new ArrayList<>();
        for (InstanceInfo instanceInfo : instanceInfos) {
            String applicationName = instanceInfo.getApplicationName();
            String instanceAddress = instanceInfo.instanceAddressWithoutProtocol();

            DefaultInstanceVO defaultInstanceVO = new DefaultInstanceVO();
            defaultInstanceVO.setApplicationName(applicationName);
            defaultInstanceVO.setInstanceIp(parseIp(instanceAddress));
            defaultInstanceVO.setInstancePort(parsePort(instanceAddress));
            defaultInstanceVOS.add(defaultInstanceVO);
        }
        return defaultInstanceVOS;
    }

    public static InstanceForHeaderVO toInstanceForHeaderVO(List<DefaultInstanceVO> defaultInstanceVOS) {
        if (CollectionUtils.isEmpty(defaultInstanceVOS)) {
            return null;
        }

        List<String> applicationNameList = defaultInstanceVOS.stream()
                .map(DefaultInstanceVO::getApplicationName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<String> instanceIpList = defaultInstanceVOS.stream()
                .map(DefaultInstanceVO::getInstanceIp)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<Integer> instancePortList = defaultInstanceVOS.stream()
                .map(DefaultInstanceVO::getInstancePort)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        InstanceForHeaderVO instanceForHeaderVO = new InstanceForHeaderVO();
        instanceForHeaderVO.setApplicationNameList(applicationNameList);
        instanceForHeaderVO.setInstanceIpList(instanceIpList);
        instanceForHeaderVO.setInstancePortList(instancePortList);

        return instanceForHeaderVO;
    }

    public static String parseIp(String instanceAddress) {
        String[] strings = instanceAddress.split(":");
        return strings[0];
    }

    public static Integer parsePort(String instanceAddress) {
        String[] strings = instanceAddress.split(":");
        return Integer.parseInt(strings[1]);
    }
}
