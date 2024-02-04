package com.suchtool.router4j.common.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Router4jPageBO {
    @ApiModelProperty("当前页")
    private long current;

    @ApiModelProperty("每页项数")
    private long size;

    @ApiModelProperty("创建开始时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty("创建结束时间")
    private LocalDateTime createTimeEnd;
}
