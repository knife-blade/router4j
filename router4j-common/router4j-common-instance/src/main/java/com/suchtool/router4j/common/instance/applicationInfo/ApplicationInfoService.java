package com.suchtool.router4j.common.instance.applicationInfo;

import com.suchtool.router4j.common.common.entity.InstanceInfo;

import java.util.List;

public interface ApplicationInfoService {
    default Boolean namespaceExist(){
        return false;
    }

    /**
     * 获得所有命名空间
     * @return 命名空间ID列表
     */
    default List<String> findAllNameSpaces(){
        return null;
    }

    /**
     * 获得所有的应用
     * @return 应用的名字列表
     */
    List<String> findAllApplications();

    /**
     * 根据服务ID获得实例
     *
     * @param applicationName 应用名字
     * @return 应用对应的所有实例
     */
    List<InstanceInfo> findInstances(String applicationName);

    /**
     * 获得所有的实例
     * @return 实例地址列表
     */
    List<String> findAllInstanceAddresses();
}
