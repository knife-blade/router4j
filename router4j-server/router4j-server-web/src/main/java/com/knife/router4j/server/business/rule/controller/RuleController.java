package com.knife.router4j.server.business.rule.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.util.PathRuleUtil;
import com.knife.router4j.server.business.rule.helper.EntityConverterHelper;
import com.knife.router4j.server.business.rule.request.RuleAddReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteByInstanceAddressReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteReq;
import com.knife.router4j.server.business.rule.request.RuleEditReq;
import com.knife.router4j.server.common.constant.ApiOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "规则配置")
@ApiSupport(order = ApiOrder.RULE)
@RestController
@RequestMapping("rule")
public class RuleController {
    @Autowired
    private PathRuleUtil pathRuleUtil;

    @ApiOperation("添加规则")
    @PostMapping("add")
    public void add(@Valid @RequestBody RuleAddReq addReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(addReq);
        pathRuleUtil.addRule(pathRuleRequest);
    }

    @ApiOperation("修改规则")
    @PostMapping("edit")
    public void edit(@Valid @RequestBody RuleEditReq editReq) {
        PathRuleRequest pathRuleRequestDelete = EntityConverterHelper
                .toPathRuleRequestDelete(editReq);
        pathRuleUtil.deleteRule(pathRuleRequestDelete);

        PathRuleRequest pathRuleRequestAdd = EntityConverterHelper
                .toPathRuleRequestAdd(editReq);
        pathRuleUtil.addRule(pathRuleRequestAdd);
    }

    @ApiOperation("删除规则（根据key）")
    @PostMapping("deleteByKey")
    public void delete(@Valid @RequestBody List<String> keys) {
        for (String key : keys) {
            pathRuleUtil.deleteRuleByKey(key);
        }
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

    @ApiOperation("查找规则")
    @GetMapping("find")
    public List<String> find(@NotBlank String serviceName,
                             @NotBlank String instanceAddress) {

        PathRuleRequest pathRuleRequest = EntityConverterHelper
                .toPathRuleRequest(serviceName, instanceAddress);

        return pathRuleUtil.findPathPatterns(pathRuleRequest);
    }
}
