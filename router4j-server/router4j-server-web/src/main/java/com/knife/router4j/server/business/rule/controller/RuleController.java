package com.knife.router4j.server.business.rule.controller;

import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.util.PathRuleUtil;
import com.knife.router4j.common.util.UrlUtil;
import com.knife.router4j.server.business.rule.helper.EntityConverterHelper;
import com.knife.router4j.server.business.rule.request.RuleAddReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteReq;
import com.knife.router4j.server.business.rule.request.RuleEditReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "规则配置")
@RestController
@RequestMapping("regulation")
public class RuleController {
    @Autowired
    private PathRuleUtil pathRuleUtil;

    @ApiOperation("添加规则")
    @PostMapping("add")
    public void add(@Valid @RequestBody RuleAddReq addReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(addReq);
        pathRuleUtil.bind(pathRuleRequest);
    }

    @ApiOperation("修改规则")
    @PostMapping("edit")
    public void edit(@Valid @RequestBody RuleEditReq editReq) {
        PathRuleRequest pathRuleRequestDelete = EntityConverterHelper
                .toPathRuleRequestDelete(editReq);
        pathRuleUtil.unbind(pathRuleRequestDelete);

        PathRuleRequest pathRuleRequestAdd = EntityConverterHelper
                .toPathRuleRequestAdd(editReq);
        pathRuleUtil.bind(pathRuleRequestAdd);
    }

    @ApiOperation("删除规则")
    @PostMapping("delete")
    public void delete(@Valid @RequestBody RuleDeleteReq deleteReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(deleteReq);
        pathRuleUtil.bind(pathRuleRequest);
    }

    @ApiOperation("查找规则")
    @GetMapping("find")
    public List<String> find(@NotBlank String serviceName,
                             @NotBlank String instanceAddress) {

        PathRuleRequest pathRuleRequest = EntityConverterHelper
                .toPathRuleRequest(serviceName, instanceAddress);

        return pathRuleUtil.findPathPatterns(pathRuleRequest);
    }
}
