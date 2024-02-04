package com.suchtool.router4j.common.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Router4jPageVO<T> {
    @ApiModelProperty("当前页")
    private long current;

    @ApiModelProperty("每页项数")
    private long size;

    @ApiModelProperty("总数量")
    private long total;

    @ApiModelProperty("数据")
    private List<T> dataList;
}
