package com.knife.router4j.server.business.rule.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("添加规则")
public class RuleDeleteReq {
    @ApiModelProperty("服务名")
    @NotBlank(message = "服务名不能为空")
    private String serviceName;

    /**
     * 实例地址。例：127.0.0.1:8080
     */
    @ApiModelProperty("实例地址")
    @NotBlank(message = "实例地址不能为空")
    private String instanceAddress;

    @ApiModelProperty("路径模板")
    @NotBlank(message = "路径模板不能为空")
    private String pathPattern;
}
