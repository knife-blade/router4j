package com.knife.router4j.server.business.guide.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("保存使用指南")
@Data
public class InstructionSaveReq {
    @ApiModelProperty("内容")
    private String content;
}
