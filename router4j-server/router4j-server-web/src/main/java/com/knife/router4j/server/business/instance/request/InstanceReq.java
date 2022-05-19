package com.knife.router4j.server.business.instance.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("添加默认实例")
@Data
public class InstanceReq {
    @ApiModelProperty("服务名")
    private String serviceName;

    @ApiModelProperty("实例地址")
    private String instanceAddress;
}
