package com.knife.router4j.server.business.instruction.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("保存使用指南")
@Data
public class InstructionSaveReq {
    @ApiModelProperty("文本数据")
    private String text;
}
