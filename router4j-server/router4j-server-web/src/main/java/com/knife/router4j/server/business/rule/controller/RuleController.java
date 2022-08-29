package com.knife.router4j.server.business.rule.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.entity.RuleInfo;
import com.knife.router4j.common.util.PathRuleUtil;
import com.knife.router4j.server.business.rule.helper.EntityConverterHelper;
import com.knife.router4j.server.business.rule.request.RuleAddReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteByInstanceAddressReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteReq;
import com.knife.router4j.server.business.rule.request.RuleEditReq;
import com.knife.router4j.server.business.rule.service.RuleService;
import com.knife.router4j.server.common.constant.ApiOrder;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "规则配置")
@ApiSupport(order = ApiOrder.RULE)
@RestController
@RequestMapping("rule")
public class RuleController {
    @Autowired
    private PathRuleUtil pathRuleUtil;

    @Autowired
    private RuleService ruleService;

    @ApiOperation("查找规则")
    @GetMapping("find")
    public PageResponse<RuleInfo> find(PathRuleRequest pathRuleRequest, PageRequest pageRequest) {
        return ruleService.find(pathRuleRequest, pageRequest);
    }

    @ApiOperation("添加规则")
    @PostMapping("add")
    public void add(@Valid @RequestBody RuleAddReq addReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(addReq);
        pathRuleUtil.addRule(pathRuleRequest);
    }

    @ApiOperation("修改规则")
    @PostMapping("edit")
    public void edit(@Valid @RequestBody RuleEditReq editReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper
                .toPathRuleRequestDelete(editReq);
        pathRuleUtil.deleteRule(pathRuleRequest);

        PathRuleRequest pathRuleRequestAdd = EntityConverterHelper
                .toPathRuleRequestAdd(editReq);
        pathRuleUtil.addRule(pathRuleRequestAdd);
    }

    @ApiOperation("删除匹配的规则")
    @PostMapping("delete")
    public void delete(@Valid @RequestBody RuleDeleteReq deleteReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(deleteReq);
        pathRuleUtil.deleteRule(pathRuleRequest);
    }

    @ApiOperation("删除指定实例的所有规则")
    @PostMapping("deleteByInstanceAddress")
    public void deleteByInstanceAddress(
            @Valid @RequestBody RuleDeleteByInstanceAddressReq deleteReq) {
        pathRuleUtil.deleteRuleByInstanceAddress(deleteReq.getInstanceAddress());
    }

    @ApiOperation("删除所有规则")
    @PostMapping("deleteAll")
    public void deleteAll() {
        pathRuleUtil.deleteAllRule();
    }
}
