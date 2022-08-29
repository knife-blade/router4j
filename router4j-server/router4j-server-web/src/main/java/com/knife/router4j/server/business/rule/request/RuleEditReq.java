package com.knife.router4j.server.business.rule.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("修改规则")
public class RuleEditReq {
    @ApiModelProperty("应用名")
    @NotBlank(message = "应用名不能为空")
    private String applicationName;

    @ApiModelProperty("旧实例地址。例：127.0.0.1:8080")
    @NotBlank(message = "旧实例地址不能为空")
    private String oldInstanceAddress;

    @ApiModelProperty("新实例地址。例：127.0.0.1:8080")
    @NotBlank(message = "新实例地址不能为空")
    private String newInstanceAddress;

    @ApiModelProperty("旧路径模板")
    @NotBlank(message = "旧路径模板不能为空")
    private String oldPathPattern;

    @ApiModelProperty("新路径模板")
    @NotBlank(message = "新路径模板不能为空")
    private String newPathPattern;
}
