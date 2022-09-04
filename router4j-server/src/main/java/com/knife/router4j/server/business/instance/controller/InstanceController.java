package com.knife.router4j.server.business.instance.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.instance.request.InstanceReq;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import com.knife.router4j.server.common.constant.ApiOrder;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("查找应用的默认实例（分页）")
    @GetMapping("findDefaultInstancePage")
    public PageResponse<InstanceVO> findDefaultInstancePage(String applicationName, PageRequest pageRequest) {
        return instanceService.findDefaultInstancePage(applicationName, pageRequest);
    }

    @ApiOperation("查找所有的应用名字")
    @GetMapping("findApplicationNames")
    public List<String> findApplicationNames(String applicationName) {
        return instanceService.findAllApplicationNames(applicationName);
    }

    @ApiOperation("设置为默认实例")
    @PostMapping("markAsDefaultInstance")
    public void markAsDefaultInstance(@RequestBody List<InstanceReq> addReqs) {
        for (InstanceReq addReq : addReqs) {
            defaultInstanceUtil.markAsDefaultInstance(
                    addReq.getApplicationName(),
                    addReq.getInstanceAddress(),
                    addReq.getForceRoute());
        }
    }

    @ApiOperation("取消默认实例设置")
    @PostMapping("cancelDefaultInstance")
    public void cancelDefaultInstance(@RequestBody List<InstanceReq> deleteReqs) {
        for (InstanceReq deleteReq : deleteReqs) {
            defaultInstanceUtil.cancelDefaultInstance(
                    deleteReq.getApplicationName(),
                    deleteReq.getInstanceAddress());
        }
    }
}
