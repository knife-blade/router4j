package com.knife.router4j.business.regulation.controller;

import com.knife.router4j.business.regulation.request.RegulationAddReq;
import com.knife.router4j.business.regulation.request.RegulationDeleteReq;
import com.knife.router4j.business.regulation.request.RegulationEditReq;
import com.knife.router4j.common.util.PathRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "规则配置")
@RestController
@RequestMapping("regulation")
public class RegulationController {
    @Autowired
    private PathRule pathRule;

    @ApiOperation("查找路径规则")
    @GetMapping("findPathPatterns")
    public List<String> findPathPatterns(@RequestParam String instanceAddress) {
        return pathRule.findPathPatterns(instanceAddress);
    }

    @ApiOperation("添加规则")
    @PostMapping("addRegulation")
    public void addRegulation(@Valid @RequestBody RegulationAddReq addReq) {
        pathRule.bind(addReq.getInstanceAddress(), addReq.getPathPattern());
    }

    @ApiOperation("修改规则")
    @PostMapping("editRegulation")
    public void editRegulation(@Valid @RequestBody RegulationEditReq editReq) {
        pathRule.unbind(editReq.getInstanceAddress(), editReq.getOldPathPattern());
        if (StringUtils.hasText(editReq.getNewPathPattern())) {
            pathRule.bind(editReq.getInstanceAddress(), editReq.getNewPathPattern());
        }
    }

    @ApiOperation("删除规则")
    @PostMapping("deleteRegulation")
    public void deleteRegulation(@Valid @RequestBody RegulationDeleteReq deleteReq) {
        pathRule.bind(deleteReq.getInstanceAddress(), deleteReq.getPathPattern());
    }
}
