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
        String instanceAddress = "";

        if (StringUtils.hasText(instanceIp)) {
            instanceAddress += instanceIp;
        }

        if (instancePort != null) {
            instanceAddress += ":" + instancePort;
        }

        List<DefaultInstanceVO> defaultInstanceVOS =
                findAllInstanceOfRegistry(applicationName, instanceIp, instancePort);

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
     * ????????????????????????????????????
     */
    private List<DefaultInstanceVO> findAllInstanceOfRegistry(String applicationName,
                                                              String instanceIp,
                                                              Integer instancePort) {
        List<InstanceInfo> instanceInfoList = new ArrayList<>();

        List<String> allApplicationNames = applicationService.findAllApplicationNames();
        for (String applicationNameOfRegistry : allApplicationNames) {
            List<InstanceInfo> instanceInfos =
                    applicationService.findInstance(applicationNameOfRegistry);
            instanceInfoList.addAll(instanceInfos);
        }

        List<DefaultInstanceVO> defaultInstanceVOS =
                InstanceHelper.toDefaultInstanceVO(instanceInfoList);

        List<DefaultInstanceVO> filterResult =
                filter(defaultInstanceVOS, applicationName, instanceIp, instancePort);
        fillIsRunning(filterResult);

        return filterResult;
    }

    /**
     * ??????????????????????????????
     *
     * @param defaultInstanceVOS ????????????
     */
    private void fillIsRunning(List<DefaultInstanceVO> defaultInstanceVOS) {
        for (DefaultInstanceVO defaultInstanceVO : defaultInstanceVOS) {
            defaultInstanceVO.setIsRunning(true);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param defaultInstanceVOS ????????????
     * @return ????????????????????????
     */
    private List<DefaultInstanceVO> findInstanceOfRedis(List<DefaultInstanceVO> defaultInstanceVOS,
                                                        String applicationName,
                                                        String instanceAddress) {
        List<DefaultInstanceVO> defaultInstanceVOListResult = new ArrayList<>(defaultInstanceVOS);

        // ???????????????Redis????????????????????????
        List<DefaultInstanceInfo> defaultInstanceListOfRedis =
                defaultInstanceUtil.findDefaultInstance(applicationName, instanceAddress);

        for (DefaultInstanceInfo defaultInstanceOfRedis : defaultInstanceListOfRedis) {
            String applicationNameOfRedis = defaultInstanceOfRedis.getApplicationName();
            String instanceAddressOfRedis = defaultInstanceOfRedis.getInstanceAddress();
            Boolean isForceRoute = defaultInstanceOfRedis.getIsForceRoute();
            String instanceIpOfRedis = InstanceHelper.parseIp(instanceAddressOfRedis);
            Integer instancePortOfRedis = InstanceHelper.parsePort(instanceAddressOfRedis);
            // ??????????????????????????????????????????
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
            // ???????????????????????????
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

    private List<DefaultInstanceVO> filter(List<DefaultInstanceVO> defaultInstanceVOList,
                                           String applicationName,
                                           String instanceIp,
                                           Integer instancePort) {
        List<DefaultInstanceVO> result = new ArrayList<>();

        for (DefaultInstanceVO defaultInstanceVO : defaultInstanceVOList) {
            boolean match = true;

            if (StringUtils.hasText(applicationName)) {
                if (!(defaultInstanceVO.getApplicationName().contains(applicationName))) {
                    match = false;
                }
            }

            if (StringUtils.hasText(instanceIp)) {
                if (!(defaultInstanceVO.getInstanceIp().contains(instanceIp))) {
                    match = false;
                }
            }

            if (instancePort != null) {
                if (!(defaultInstanceVO.getInstancePort().toString()
                        .contains(instancePort.toString()))) {
                    match = false;
                }
            }

            if (match) {
                result.add(defaultInstanceVO);
            }
        }

        return result;
    }
}
