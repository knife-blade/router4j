package com.knife.router4j.server.business.instance.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.instance.request.InstanceReq;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import com.knife.router4j.server.common.constant.ApiOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "实例管理")
@ApiSupport(order = ApiOrder.INSTANCE)
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
        defaultInstanceUtil.markAsDefaultInstance(
                addReq.getApplicationName(),
                addReq.getInstanceAddress(),
                addReq.getForceRoute());
    }

    @ApiOperation("取消默认实例设置")
    @PostMapping("cancelDefaultInstance")
    public void cancelDefaultInstance(@RequestBody InstanceReq deleteReq) {
        defaultInstanceUtil.cancelDefaultInstance(
                deleteReq.getApplicationName(),
                deleteReq.getInstanceAddress());
    }

    @ApiOperation("查找应用的默认实例")
    @PostMapping("findDefaultInstance")
    public List<InstanceVO> findDefaultInstance(String applicationName) {
        return instanceService.findDefaultInstance(applicationName);
    }
}
