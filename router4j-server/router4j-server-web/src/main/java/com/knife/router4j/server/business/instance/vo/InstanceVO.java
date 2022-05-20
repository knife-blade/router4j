package com.knife.router4j.server.business.instance.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("添加默认实例")
@Data
public class InstanceVO {
    @ApiModelProperty("实例地址")
    private String instanceAddress;

    @ApiModelProperty("是否是默认实例")
    private Boolean isDefaultInstance;
}
