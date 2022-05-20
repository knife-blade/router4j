package com.knife.router4j.server.business.instance.controller;

import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.instance.request.InstanceReq;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "实例管理")
@RestController
@RequestMapping("instance")
public class InstanceController {
    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    @Autowired
    private InstanceService instanceService;

    @ApiOperation("设置为默认实例")
    @PostMapping("markAsDefaultInstance")
    public void markAsDefaultInstance(@RequestBody InstanceReq addReq) {
        defaultInstanceUtil.markAsDefaultInstance(addReq.getInstanceAddresses());
    }

    @ApiOperation("取消默认实例设置")
    @PostMapping("cancelDefaultInstance")
    public void cancelDefaultInstance(@RequestBody InstanceReq deleteReq) {
        defaultInstanceUtil.cancelDefaultInstance(deleteReq.getInstanceAddresses());
    }

    @ApiOperation("查找所有实例")
    @PostMapping("findAllDefaultInstance")
    public List<InstanceVO> findAllDefaultInstance() {
        return instanceService.findInstances();
    }
}
