package com.suchtool.router4j.server.business.instance.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实例信息（包括在Redis里的）
 */
@Data
public class InstanceBO {
    @ApiModelProperty("命名空间")
    private String namespaceName;

    @ApiModelProperty("应用名字")
    private String applicationName;

    @ApiModelProperty("实例的IP")
    private String instanceIp;

    @ApiModelProperty("实例的端口")
    private Integer instancePort;
}
