package com.suchtool.router4j.server.business.instance.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("默认实例")
@Data
public class DefaultInstanceVO {
    @ApiModelProperty("应用名字")
    private String applicationName;

    @ApiModelProperty("实例IP")
    private String instanceIp;

    @ApiModelProperty("实例端口")
    private Integer instancePort;

    @ApiModelProperty("是否是默认实例")
    private Boolean isDefaultInstance;

    @ApiModelProperty("是否强制")
    private Boolean isForceRoute;

    @ApiModelProperty("是否正在运行")
    private Boolean isRunning;
}
