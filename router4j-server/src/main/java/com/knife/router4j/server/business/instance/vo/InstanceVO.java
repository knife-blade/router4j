package com.knife.router4j.server.business.instance.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("实例")
@Data
public class InstanceVO {
    @ApiModelProperty("应用名字")
    private String applicationName;

    @ApiModelProperty("实例地址")
    private String instanceAddress;

    @ApiModelProperty("是否是默认实例")
    private Boolean isDefaultInstance;

    @ApiModelProperty("是否正在运行")
    private Boolean isRunning;
}
