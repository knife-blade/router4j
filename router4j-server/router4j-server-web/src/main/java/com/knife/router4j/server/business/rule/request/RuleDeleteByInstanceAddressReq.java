package com.knife.router4j.server.business.rule.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("添加规则")
public class RuleDeleteByInstanceAddressReq {
    @ApiModelProperty("实例地址。例：127.0.0.1:8080")
    @NotBlank(message = "实例地址不能为空")
    private String instanceAddress;
}
