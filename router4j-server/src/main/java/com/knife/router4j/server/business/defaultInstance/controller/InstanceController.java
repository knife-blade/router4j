package com.knife.router4j.server.business.defaultInstance.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.defaultInstance.request.InstanceReq;
import com.knife.router4j.server.business.defaultInstance.service.InstanceService;
import com.knife.router4j.server.business.defaultInstance.vo.DefaultInstanceVO;
import com.knife.router4j.server.business.defaultInstance.vo.InstanceVO;
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

    @ApiOperation("查找所有的默认应用")
    @GetMapping("findApplicationNames")
    public List<InstanceVO> findApplicationNames(InstanceVO instanceVO) {
        return instanceService.findAllInstance(instanceVO);
    }

    @ApiOperation("查找应用的默认实例（分页）")
    @GetMapping("findDefaultInstancePage")
    public PageResponse<DefaultInstanceVO> findDefaultInstancePage(InstanceVO instanceVO, PageRequest pageRequest) {
        return instanceService.findDefaultInstancePage(instanceVO, pageRequest);
    }

    @ApiOperation("设置默认实例")
    @PostMapping("setupDefaultInstance")
    public void setupDefaultInstance(@RequestBody List<InstanceReq> addReqs) {
        instanceService.setupDefaultInstance(addReqs);
    }
}
