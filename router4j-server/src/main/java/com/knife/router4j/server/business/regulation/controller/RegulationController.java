package com.knife.router4j.server.business.regulation.controller;

import com.knife.router4j.common.entity.InstanceAddress;
import com.knife.router4j.common.util.PathRule;
import com.knife.router4j.common.util.UrlUtil;
import com.knife.router4j.server.business.regulation.request.RegulationAddReq;
import com.knife.router4j.server.business.regulation.request.RegulationDeleteReq;
import com.knife.router4j.server.business.regulation.request.RegulationEditReq;
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

    @Autowired
    private UrlUtil urlUtil;

    @ApiOperation("查找路径规则")
    @GetMapping("findPathPatterns")
    public List<String> findPathPatterns(@RequestParam String instanceAddress) {
        InstanceAddress instance = urlUtil.parse(instanceAddress);
        return pathRule.findPathPatterns(instance);
    }

    @ApiOperation("添加规则")
    @PostMapping("addRegulation")
    public void addRegulation(@Valid @RequestBody RegulationAddReq addReq) {
        InstanceAddress instance = urlUtil.parse(addReq.getInstanceAddress());
        pathRule.bind(instance, addReq.getPathPattern());
    }

    @ApiOperation("修改规则")
    @PostMapping("editRegulation")
    public void editRegulation(@Valid @RequestBody RegulationEditReq editReq) {
        InstanceAddress instance = urlUtil.parse(editReq.getInstanceAddress());

        pathRule.unbind(instance, editReq.getOldPathPattern());
        if (StringUtils.hasText(editReq.getNewPathPattern())) {
            pathRule.bind(instance, editReq.getNewPathPattern());
        }
    }

    @ApiOperation("删除规则")
    @PostMapping("deleteRegulation")
    public void deleteRegulation(@Valid @RequestBody RegulationDeleteReq deleteReq) {
        InstanceAddress instance = urlUtil.parse(deleteReq.getInstanceAddress());
        pathRule.bind(instance, deleteReq.getPathPattern());
    }
}
