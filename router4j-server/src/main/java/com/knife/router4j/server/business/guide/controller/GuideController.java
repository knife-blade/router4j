package com.knife.router4j.server.business.guide.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.util.GuideUtil;
import com.knife.router4j.server.business.guide.request.InstructionSaveReq;
import com.knife.router4j.server.common.constant.ApiOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "使用指南")
@ApiSupport(order = ApiOrder.GUIDE)
@RestController
@RequestMapping("guide")
public class GuideController {
    @Autowired
    private GuideUtil guideUtil;

    @ApiOperation("保存")
    @PostMapping("save")
    public void save(@RequestBody InstructionSaveReq saveReq) {
        guideUtil.save(saveReq.getText());
    }

    @ApiOperation("查看")
    @GetMapping("find")
    public String find() {
        return guideUtil.find();
    }
}
