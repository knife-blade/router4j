package com.knife.router4j.business.application;

import com.knife.router4j.business.application.util.NacosUtil;
import com.knife.router4j.business.application.vo.Instance;
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
    private NacosUtil nacosUtil;

    @ApiOperation("获取所有应用的信息")
    @GetMapping("getAllApplication")
    public List<String> getAllApplication() {
        return nacosUtil.getServices();
    }

    @ApiOperation("根据服务名获得实例")
    @GetMapping("getInstance")
    public List<Instance> getInstance(@RequestParam String serviceName) {
        return nacosUtil.getInstances(serviceName);
    }

}
