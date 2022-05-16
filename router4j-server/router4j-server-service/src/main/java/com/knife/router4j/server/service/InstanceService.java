package com.knife.router4j.server.service;

import com.knife.router4j.common.entity.InstanceInfo;

import java.util.List;

public interface InstanceService {
    /**
     * 获得所有的服务
     * @return 服务的名字列表
     */
    List<String> findAllServices();

    /**
     * 根据服务ID获得实例
     *
     * @param serviceId 服务ID，一般是服务名字
     * @return 服务对应的所有实例
     */
    List<InstanceInfo> findInstances(String serviceId);
}
