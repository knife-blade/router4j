package com.knife.router4j.example.order.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order{
    private Long id;

    // 用户id
    private Long userId;

    // 产品id
    private Long productId;

    // 数量
    private Integer count;

    // 金额
    private BigDecimal money;

    // 订单状态：0：创建中；1：已完结
    private Integer status;

    private LocalDateTime createTime;

    // 插入与更新都写此字段。若使用FieldFill.UPDATE，则只更新时写此字段。
    private LocalDateTime updateTime;

    public Order(Long id, Long userId, Long productId, Integer count, BigDecimal money, Integer status) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.count = count;
        this.money = money;
        this.status = status;
    }
}