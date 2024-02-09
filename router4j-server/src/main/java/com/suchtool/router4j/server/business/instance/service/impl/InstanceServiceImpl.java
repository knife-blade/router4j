package com.suchtool.router4j.server.business.instance.service.impl;

import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.common.util.Router4jPageUtil;
import com.suchtool.router4j.common.entity.DefaultInstanceInfo;
import com.suchtool.router4j.common.util.DefaultInstanceUtil;
import com.suchtool.router4j.server.business.application.service.ApplicationService;
import com.suchtool.router4j.server.business.instance.helper.InstanceHelper;
import com.suchtool.router4j.server.business.instance.bo.DefaultInstanceBO;
import com.suchtool.router4j.server.business.instance.service.InstanceService;
import com.suchtool.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.suchtool.router4j.server.business.instance.bo.InstanceBO;
import com.suchtool.router4j.server.business.instance.vo.InstanceForHeaderVO;
import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    public InstanceForHeaderVO findAllInstance(InstanceBO instanceBO) {
        List<DefaultInstanceVO> defaultInstanceList = findDefaultInstance(
                instanceBO);

        return InstanceHelper.toInstanceForHeaderVO(defaultInstanceList);
    }

    @Override
    public Router4jPageVO<DefaultInstanceVO> findDefaultInstancePage(InstanceBO instanceBO,
                                                                     Router4jPageBO router4jPageBO) {
        List<DefaultInstanceVO> defaultInstanceList = findDefaultInstance(instanceBO);
        return Router4jPageUtil.toPage(defaultInstanceList, router4jPageBO);
    }

    @Override
    public void setupDefaultInstance(List<DefaultInstanceBO> defaultInstanceBOS) {
        for (DefaultInstanceBO defaultInstanceBO : defaultInstanceBOS) {
            String applicationName = defaultInstanceBO.getApplicationName();

            String instanceAddress = null;
            if (StringUtils.hasText(defaultInstanceBO.getInstanceIp())
                    || defaultInstanceBO.getInstancePort() != null) {
                instanceAddress = defaultInstanceBO.getInstanceIp() + ":" + defaultInstanceBO.getInstancePort();
            }

            if (defaultInstanceBO.getIsForceRoute() != null
                    && defaultInstanceBO.getIsForceRoute()) {
                defaultInstanceUtil.markAsDefaultInstance(
                        applicationName, instanceAddress, true);
            } else {
                if (defaultInstanceBO.getIsDefaultInstance() != null
                        && defaultInstanceBO.getIsDefaultInstance()) {
                    defaultInstanceUtil.markAsDefaultInstance(
                            applicationName, instanceAddress, false);
                } else {
                    defaultInstanceUtil.cancelDefaultInstance(applicationName, instanceAddress);
                }
            }
        }
    }

    private List<DefaultInstanceVO> findDefaultInstance(InstanceBO instanceBO) {
        String instanceAddress = "";

        if (StringUtils.hasText(instanceBO.getInstanceIp())) {
            instanceAddress += instanceBO.getInstanceIp();
        }

        if (instanceBO.getInstancePort() != null) {
            instanceAddress += ":" + instanceBO.getInstancePort();
        }

        List<DefaultInstanceVO> defaultInstanceVOS = findAllInstanceOfRegistry(instanceBO);
        if (CollectionUtils.isEmpty(defaultInstanceVOS)) {
            return defaultInstanceVOS;
        }

        List<DefaultInstanceVO> defaultInstanceVOListResult = findInstanceOfRedis(
                defaultInstanceVOS, instanceBO.getApplicationName(), instanceAddress);

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
    private List<DefaultInstanceVO> findAllInstanceOfRegistry(InstanceBO instanceBO) {
        List<InstanceInfo> instanceInfoList = new ArrayList<>();

        List<String> allApplicationNames = applicationService.findAllApplicationNames(
                instanceBO.getNamespaceName());
        if (CollectionUtils.isEmpty(allApplicationNames)) {
            return null;
        }

        for (String applicationNameOfRegistry : allApplicationNames) {
            List<InstanceInfo> instanceInfos = applicationService.findInstance(
                    instanceBO.getNamespaceName(), applicationNameOfRegistry);
            if (instanceInfos != null) {
                instanceInfoList.addAll(instanceInfos);
            }
        }

        List<DefaultInstanceVO> defaultInstanceVOS =
                InstanceHelper.toDefaultInstanceVO(instanceInfoList);

        List<DefaultInstanceVO> filterResult = filter(
                defaultInstanceVOS,
                instanceBO.getApplicationName(),
                instanceBO.getInstanceIp(),
                instanceBO.getInstancePort());
        fillIsRunning(filterResult);

        return filterResult;
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
        if (CollectionUtils.isEmpty(defaultInstanceVOS)) {
            return defaultInstanceVOS;
        }

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
