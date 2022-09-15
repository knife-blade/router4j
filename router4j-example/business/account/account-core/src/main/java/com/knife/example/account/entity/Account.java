package com.knife.example.account.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Account{
    // id
    private Long id;

    // 用户id
    private Long userId;

    // 总额度
    private BigDecimal total;

    // 已用余额
    private BigDecimal used;

    // 剩余可用额度
    private BigDecimal residue;

    // 冻结金额
    private BigDecimal frozen;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}