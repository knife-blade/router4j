package com.knife.router4j.server.business.regulation.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 删除规则
 */
@Data
@ApiModel("添加规则")
public class RegulationDeleteReq {
    /**
     * 实例地址。例：127.0.0.1:8080
     */
    @ApiModelProperty("实例地址")
    @NotBlank(message = "实例地址不能为空")
    private String InstanceInfo;

    /**
     * 路径匹配表达式
     */
    @ApiModelProperty("路径匹配表达式")
    private String pathPattern;
}
