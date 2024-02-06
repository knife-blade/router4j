package com.suchtool.router4j.server.business.application.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.ApplicationPageBO;
import com.suchtool.router4j.common.instance.applicationInfo.bo.InstancePageBO;
import com.suchtool.router4j.common.instance.applicationInfo.service.ApplicationInfoService;
import com.suchtool.router4j.common.instance.applicationInfo.vo.ApplicationVO;
import com.suchtool.router4j.server.common.constant.ApiOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "应用管理")
@ApiSupport(order = ApiOrder.APPLICATION)
@RestController
@RequestMapping("application")
public class ApplicationController {
    @Autowired
    private ApplicationInfoService applicationInfoService;

    @ApiOperation("是否存在命名空间")
    @GetMapping("checkNamespaceExist")
    public Boolean checkNamespaceExist() {
        return applicationInfoService.checkNamespaceExist();
    }

    @ApiOperation("获取所有的命名空间")
    @GetMapping("findAllNamespaces")
    public List<String> findAllNamespaces() {
        return applicationInfoService.findAllNamespaces();
    }

    @ApiOperation("获取所有的应用")
    @GetMapping("findAllApplication")
    public Router4jPageVO<ApplicationVO> findAllApplication(ApplicationPageBO applicationPageBO) {
        return applicationInfoService.findAllApplications(applicationPageBO);
    }

    @ApiOperation("根据应用名获得实例")
    @GetMapping("findInstance")
    public Router4jPageVO<InstanceInfo> findInstance(InstancePageBO instancePageBO) {
        return applicationInfoService.findInstances(instancePageBO);
    }
}
