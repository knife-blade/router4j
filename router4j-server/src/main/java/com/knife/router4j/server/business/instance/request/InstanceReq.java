package com.knife.router4j.server.business.instance.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("添加默认实例")
@Data
public class InstanceReq {
    @ApiModelProperty("应用名字")
    private String applicationName;

    @ApiModelProperty("实例地址")
    @NotBlank(message = "实例地址不能为空")
    private String instanceAddress;

    @ApiModelProperty("是否设置为默认路由")
    @NotNull(message = "是否设置为默认路由不能为空")
    private Boolean isDefaultInstance;

    @ApiModelProperty("是否设置为强制路由")
    @NotNull(message = "是否设置为强制路由不能为空")
    private Boolean isForceRoute;
}
