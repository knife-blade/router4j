package com.knife.router4j.business.application;

import com.knife.router4j.business.application.util.NacosUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "应用")
@RestController
@RequestMapping("application")
public class ApplicationController {

    @ApiOperation("获取所有应用的信息")
    @GetMapping("getAllApplication")
    public void getAllApplication() {
        NacosUtil.onEvent(null);
    }
}
