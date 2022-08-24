package com.knife.router4j.server.business.application.controller;

import com.knife.router4j.common.entity.InstanceInfo;
import com.knife.router4j.server.service.ApplicationInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "应用")
@RestController
@RequestMapping("application")
public class ApplicationController {
    @Autowired
    private ApplicationInfoService applicationInfoService;

    @ApiOperation("获取所有应用的信息")
    @GetMapping("findAllApplication")
    public List<String> findAllApplication() {
        return applicationInfoService.findAllServices();
    }

    @ApiOperation("根据服务名获得实例")
    @GetMapping("findInstance")
    public List<InstanceInfo> findInstance(@RequestParam String serviceName) {
        return applicationInfoService.findInstances(serviceName);
    }
}
