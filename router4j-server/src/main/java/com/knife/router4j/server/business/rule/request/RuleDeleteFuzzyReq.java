package com.knife.router4j.server.business.rule.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("删除规则（模糊）")
public class RuleDeleteFuzzyReq {
    @ApiModelProperty("应用名")
    private String applicationName;

    @ApiModelProperty("实例地址。例：127.0.0.1:8080")
    private String instanceAddress;

    @ApiModelProperty("路径模板")
    private String pathPattern;
}
