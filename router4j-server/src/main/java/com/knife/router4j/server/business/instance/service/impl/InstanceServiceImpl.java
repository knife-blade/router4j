package com.knife.router4j.server.business.instance.service.impl;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.entity.DefaultInstanceInfo;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.instance.helper.InstanceHelper;
import com.knife.router4j.server.business.instance.request.DefaultInstanceRequest;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.knife.router4j.server.business.instance.request.InstanceRequest;
import com.knife.router4j.server.business.instance.vo.InstanceForHeaderVO;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;
import com.knife.router4j.server.common.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    @Override
    public InstanceForHeaderVO findAllInstance(InstanceRequest instanceRequest) {
        List<DefaultInstanceVO> defaultInstanceList = findDefaultInstance(
                instanceRequest.getApplicationName(),
                instanceRequest.getInstanceIp(),
                instanceRequest.getInstancePort());

        return InstanceHelper.toInstanceForHeaderVO(defaultInstanceList);
    }

    @Override
    public PageResponse<DefaultInstanceVO> findDefaultInstancePage(InstanceRequest instanceRequest,
                                                                   PageRequest pageRequest) {
        List<DefaultInstanceVO> defaultInstanceList = findDefaultInstance(
                instanceRequest.getApplicationName(),
                instanceRequest.getInstanceIp(),
                instanceRequest.getInstancePort());
        return PageUtil.toPage(defaultInstanceList, pageRequest);
    }

    @Override
    public void setupDefaultInstance(List<DefaultInstanceRequest> defaultInstanceRequests) {
        for (DefaultInstanceRequest defaultInstanceRequest : defaultInstanceRequests) {
            String applicationName = defaultInstanceRequest.getApplicationName();

            String instanceAddress = null;
            if (StringUtils.hasText(defaultInstanceRequest.getInstanceIp())
                    || defaultInstanceRequest.getInstancePort() != null) {
                instanceAddress = defaultInstanceRequest.getInstanceIp() + ":" + defaultInstanceRequest.getInstancePort();
            }

            if (defaultInstanceRequest.getIsForceRoute() != null
                    && defaultInstanceRequest.getIsForceRoute()) {
                defaultInstanceUtil.markAsDefaultInstance(
                        applicationName, instanceAddress, true);
            } else {
                if (defaultInstanceRequest.getIsDefaultInstance() != null
                        && defaultInstanceRequest.getIsDefaultInstance()) {
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

        List<DefaultInstanceVO> defaultInstanceVOS = InstanceHelper.toDefaultInstanceVO(instanceInfos);
        fillIsRunning(defaultInstanceVOS);

        List<DefaultInstanceVO> defaultInstanceVOListResult = findInstanceOfRedis(
                defaultInstanceVOS, applicationName, instanceAddress);

        defaultInstanceVOListResult = defaultInstanceVOListResult.stream()
                .sorted(((o1, o2) -> {
                    String tmp1 = o1.getApplicationName() + o1.getInstanceIp() + o1.getInstancePort();
                    String tmp2 = o2.getApplicationName() + o2.getInstanceIp() + o2.getInstancePort();
                    return tmp1.compareTo(tmp2);
                }))
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
            String instanceIpOfRedis = InstanceHelper.parseIp(instanceAddressOfRedis);
            Integer instancePortOfRedis = InstanceHelper.parsePort(instanceAddressOfRedis);
            // 如果结果中已存在，则不再添加
            boolean exist = false;
            for (DefaultInstanceVO defaultInstanceVO : defaultInstanceVOListResult) {
                if (applicationNameOfRedis.equals(defaultInstanceVO.getApplicationName())
                        && instanceIpOfRedis.equals(defaultInstanceVO.getInstanceIp())
                        && instancePortOfRedis.equals(defaultInstanceVO.getInstancePort())) {
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
                defaultInstanceVO.setInstanceIp(InstanceHelper.parseIp(instanceAddressOfRedis));
                defaultInstanceVO.setInstancePort(InstanceHelper.parsePort(instanceAddressOfRedis));
                defaultInstanceVO.setIsDefaultInstance(true);
                defaultInstanceVO.setIsForceRoute(isForceRoute);
                defaultInstanceVO.setIsRunning(false);
                defaultInstanceVOListResult.add(defaultInstanceVO);
            }
        }
        return defaultInstanceVOListResult;
    }
}
