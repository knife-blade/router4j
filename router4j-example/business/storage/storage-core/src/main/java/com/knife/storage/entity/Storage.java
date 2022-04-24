package com.knife.storage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class Storage{
    private Long id;

    // 产品id
    private Long productId;

    // 总库存
    private Integer total;

    // 已用库存
    private Integer used;

    // 剩余库存
    private Integer residue;

    // 冻结库存
    private Integer frozen;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}