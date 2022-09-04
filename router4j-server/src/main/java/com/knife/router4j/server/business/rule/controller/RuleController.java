package com.knife.router4j.server.business.rule.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.knife.router4j.common.entity.PathPatternInfo;
import com.knife.router4j.common.entity.PathRuleRequest;
import com.knife.router4j.common.util.ServerPathRuleUtil;
import com.knife.router4j.server.business.rule.helper.EntityConverterHelper;
import com.knife.router4j.server.business.rule.request.RuleAddReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteAccurateReq;
import com.knife.router4j.server.business.rule.request.RuleDeleteFuzzyReq;
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
import java.util.List;

@Api(tags = "规则配置")
@ApiSupport(order = ApiOrder.RULE)
@RestController
@RequestMapping("rule")
public class RuleController {
    @Autowired
    private ServerPathRuleUtil serverPathRuleUtil;

    @Autowired
    private RuleService ruleService;

    @ApiOperation("查找规则")
    @GetMapping("page")
    public PageResponse<PathPatternInfo> page(PathRuleRequest pathRuleRequest, PageRequest pageRequest) {
        return ruleService.page(pathRuleRequest, pageRequest);
    }

    @ApiOperation("查找应用名字")
    @GetMapping("findApplicationNames")
    public List<String> findApplicationNames() {
        return ruleService.findAllApplicationNames();
    }

    @ApiOperation("查找实例地址")
    @GetMapping("findInstanceAddresses")
    public List<String> findInstanceAddresses(String applicationName) {
        return ruleService.findInstanceAddresses(applicationName);
    }

    @ApiOperation("添加规则")
    @PostMapping("add")
    public void add(@Valid @RequestBody RuleAddReq addReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(addReq);
        serverPathRuleUtil.addRule(pathRuleRequest);
    }

    @ApiOperation("修改规则")
    @PostMapping("edit")
    public void edit(@Valid @RequestBody RuleEditReq editReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper
                .toPathRuleRequestDelete(editReq);
        serverPathRuleUtil.deleteRuleAccurate(pathRuleRequest);

        PathRuleRequest pathRuleRequestAdd = EntityConverterHelper
                .toPathRuleRequestAdd(editReq);
        serverPathRuleUtil.addRule(pathRuleRequestAdd);
    }

    @ApiOperation("删除（精准）")
    @PostMapping("deleteAccurate")
    public void deleteAccurate(@Valid @RequestBody RuleDeleteAccurateReq deleteReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(deleteReq);
        serverPathRuleUtil.deleteRuleAccurate(pathRuleRequest);
    }

    @ApiOperation("批量删除（精准）")
    @PostMapping("deleteAccurateBatch")
    public void deleteAccurateBatch(@Valid @RequestBody List<RuleDeleteAccurateReq> deleteBatchReq) {
        for (RuleDeleteAccurateReq deleteReq : deleteBatchReq) {
            PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(deleteReq);
            serverPathRuleUtil.deleteRuleAccurate(pathRuleRequest);
        }
    }

    @ApiOperation("删除（模糊）")
    @PostMapping("deleteFuzzy")
    public void deleteFuzzy(@Valid @RequestBody RuleDeleteFuzzyReq ruleDeleteFuzzyReq) {
        PathRuleRequest pathRuleRequest = EntityConverterHelper.toPathRuleRequest(ruleDeleteFuzzyReq);
        serverPathRuleUtil.deleteRuleFuzzy(pathRuleRequest);
    }

    @ApiOperation("删除所有规则")
    @PostMapping("deleteAll")
    public void deleteAll() {
        serverPathRuleUtil.deleteAllRule();
    }
}
