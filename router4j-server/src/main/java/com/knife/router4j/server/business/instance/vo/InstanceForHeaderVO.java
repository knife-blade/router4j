package com.knife.router4j.server.business.instance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 实例信息（包括在Redis里的）
 */
@Data
public class InstanceForHeaderVO {
    @ApiModelProperty("应用名字")
    private List<String> applicationNameList;

    @ApiModelProperty("实例的IP")
    private List<String> instanceIpList;

    @ApiModelProperty("实例的端口")
    private List<Integer> instancePortList;
}
