package com.knife.router4j.server.business.instance.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.instance.request.DefaultInstanceRequest;
import com.knife.router4j.server.business.instance.service.InstanceService;
import com.knife.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.knife.router4j.server.business.instance.request.InstanceRequest;
import com.knife.router4j.server.business.instance.vo.InstanceForHeaderVO;
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
    @GetMapping("findAllInstance")
    public InstanceForHeaderVO findAllInstance(InstanceRequest instanceRequest) {
        return instanceService.findAllInstance(instanceRequest);
    }

    @ApiOperation("查找应用的默认实例（分页）")
    @GetMapping("findDefaultInstancePage")
    public PageResponse<DefaultInstanceVO> findDefaultInstancePage(InstanceRequest instanceRequest, PageRequest pageRequest) {
        return instanceService.findDefaultInstancePage(instanceRequest, pageRequest);
    }

    @ApiOperation("设置默认实例")
    @PostMapping("setupDefaultInstance")
    public void setupDefaultInstance(@RequestBody List<DefaultInstanceRequest> addReqs) {
        instanceService.setupDefaultInstance(addReqs);
    }
}
