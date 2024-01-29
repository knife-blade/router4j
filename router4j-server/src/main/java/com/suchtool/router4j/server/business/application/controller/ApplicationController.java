package com.suchtool.router4j.server.business.application.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.suchtool.router4j.common.common.entity.InstanceInfo;
import com.suchtool.router4j.common.instance.applicationInfo.ApplicationInfoService;
import com.suchtool.router4j.server.common.constant.ApiOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "应用管理")
@ApiSupport(order = ApiOrder.APPLICATION)
@RestController
@RequestMapping("application")
public class ApplicationController {
    @Autowired
    private ApplicationInfoService applicationInfoService;

    @ApiOperation("获取所有的应用")
    @GetMapping("findAllApplication")
    public List<String> findAllApplication() {
        return applicationInfoService.findAllApplications();
    }

    @ApiOperation("根据应用名获得实例")
    @GetMapping("findInstance")
    public List<InstanceInfo> findInstance(@RequestParam String applicationName) {
        return applicationInfoService.findInstances(applicationName);
    }
}
