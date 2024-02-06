package com.suchtool.router4j.common.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Router4jPageBO {
    @ApiModelProperty("当前页")
    private long pageNo;

    @ApiModelProperty("每页项数")
    private long pageSize = 10;
}
