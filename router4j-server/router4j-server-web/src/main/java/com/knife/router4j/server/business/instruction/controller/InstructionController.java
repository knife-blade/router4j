package com.knife.router4j.server.business.instruction.controller;

import com.knife.router4j.common.util.InstructionUtil;
import com.knife.router4j.server.business.instruction.request.InstructionSaveReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "使用指南")
@RestController
@RequestMapping("instruction")
public class InstructionController {
    @Autowired
    private InstructionUtil instructionUtil;

    @ApiOperation("保存使用指南")
    @PostMapping("save")
    public void save(@RequestBody InstructionSaveReq saveReq) {
        instructionUtil.save(saveReq.getText());
    }

    @ApiOperation("查看使用指南")
    @GetMapping("find")
    public String find() {
        return instructionUtil.find();
    }
}
