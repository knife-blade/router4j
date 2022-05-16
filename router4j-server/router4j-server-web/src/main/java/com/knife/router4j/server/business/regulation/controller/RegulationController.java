package com.knife.router4j.server.business.regulation.controller;

import com.knife.router4j.common.entity.InstanceInfo;
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

    @ApiOperation("添加规则")
    @PostMapping("add")
    public void add(@Valid @RequestBody RegulationAddReq addReq) {
        InstanceInfo instance = urlUtil.parse(addReq.getInstanceInfo());
        pathRule.bind(instance, addReq.getPathPattern());
    }

    @ApiOperation("修改规则")
    @PostMapping("edit")
    public void edit(@Valid @RequestBody RegulationEditReq editReq) {
        InstanceInfo instance = urlUtil.parse(editReq.getInstanceInfo());

        pathRule.unbind(instance, editReq.getOldPathPattern());
        if (StringUtils.hasText(editReq.getNewPathPattern())) {
            pathRule.bind(instance, editReq.getNewPathPattern());
        }
    }

    @ApiOperation("删除规则")
    @PostMapping("delete")
    public void delete(@Valid @RequestBody RegulationDeleteReq deleteReq) {
        InstanceInfo instance = urlUtil.parse(deleteReq.getInstanceInfo());
        pathRule.bind(instance, deleteReq.getPathPattern());
    }

    @ApiOperation("查找规则")
    @GetMapping("find")
    public List<String> find(@RequestParam String InstanceInfo) {
        InstanceInfo instance = urlUtil.parse(InstanceInfo);
        return pathRule.findPathPatterns(instance);
    }
}
