package com.knife.router4j.server.business.instance.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实例信息（包括在Redis里的）
 */
@Data
public class InstanceRequest {
    @ApiModelProperty("应用名字")
    private String applicationName;

    @ApiModelProperty("实例的IP")
    private String instanceIp;

    @ApiModelProperty("实例的端口")
    private Integer instancePort;
}
