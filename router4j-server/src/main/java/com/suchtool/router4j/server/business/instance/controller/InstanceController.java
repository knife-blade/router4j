package com.suchtool.router4j.server.business.instance.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.suchtool.router4j.common.util.DefaultInstanceUtil;
import com.suchtool.router4j.server.business.instance.bo.DefaultInstanceBO;
import com.suchtool.router4j.server.business.instance.service.InstanceService;
import com.suchtool.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.suchtool.router4j.server.business.instance.bo.InstanceBO;
import com.suchtool.router4j.server.business.instance.vo.InstanceForHeaderVO;
import com.suchtool.router4j.server.common.constant.ApiOrder;
import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
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
    public InstanceForHeaderVO findAllInstance(InstanceBO instanceBO) {
        return instanceService.findAllInstance(instanceBO);
    }

    @ApiOperation("查找应用的默认实例（分页）")
    @GetMapping("findDefaultInstancePage")
    public Router4jPageVO<DefaultInstanceVO> findDefaultInstancePage(InstanceBO instanceBO, Router4jPageBO router4jPageBO) {
        return instanceService.findDefaultInstancePage(instanceBO, router4jPageBO);
    }

    @ApiOperation("设置默认实例")
    @PostMapping("setupDefaultInstance")
    public void setupDefaultInstance(@RequestBody List<DefaultInstanceBO> addReqs) {
        instanceService.setupDefaultInstance(addReqs);
    }
}
