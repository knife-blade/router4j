package com.suchtool.router4j.common.instance.applicationInfo.service;

import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.ApplicationPageBO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.InstancePageBO;
import com.suchtool.router4j.common.instance.applicationInfo.vo.ApplicationVO;

import java.util.List;

public interface ApplicationInfoService {
    default Boolean checkNamespaceExist(){
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
     *
     * @param applicationPageBO 命名空间名字
     * @return 应用列表
     */
    Router4jPageVO<ApplicationVO> findAllApplications(ApplicationPageBO applicationPageBO);

    /**
     * 根据服务ID获得实例
     *
     * @param instancePageBO@return 应用对应的所有实例
     */
    Router4jPageVO<InstanceInfo> findInstances(InstancePageBO instancePageBO);

}
