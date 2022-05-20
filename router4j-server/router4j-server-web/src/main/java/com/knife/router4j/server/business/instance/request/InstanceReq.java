package com.knife.router4j.server.business.instance.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("添加默认实例")
@Data
public class InstanceReq {
    @ApiModelProperty("实例地址")
    private List<String> instanceAddresses;
}
