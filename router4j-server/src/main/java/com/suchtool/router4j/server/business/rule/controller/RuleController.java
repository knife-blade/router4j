package com.suchtool.router4j.server.business.rule.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.suchtool.router4j.rule.entity.PathPatternInfo;
import com.suchtool.router4j.rule.entity.PathRuleRequest;
import com.suchtool.router4j.rule.util.ServerPathRuleUtil;
import com.suchtool.router4j.server.business.rule.helper.EntityConverterHelper;
import com.suchtool.router4j.server.business.rule.request.RuleAddReq;
import com.suchtool.router4j.server.business.rule.request.RuleDeleteAccurateReq;
import com.suchtool.router4j.server.business.rule.request.RuleDeleteFuzzyReq;
import com.suchtool.router4j.server.business.rule.request.RuleEditReq;
import com.suchtool.router4j.server.business.rule.service.RuleService;
import com.suchtool.router4j.server.common.constant.ApiOrder;
import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;
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
    public Router4jPageVO<PathPatternInfo> page(PathRuleRequest pathRuleRequest, Router4jPageBO router4jPageBO) {
        return ruleService.page(pathRuleRequest, router4jPageBO);
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
