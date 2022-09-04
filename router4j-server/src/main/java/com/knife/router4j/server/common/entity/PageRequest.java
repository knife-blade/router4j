package com.knife.router4j.server.common.entity;

import lombok.Data;

@Data
public class PageRequest {
    /**
     * 每页的大小
     */
    private long size;

    /**
     * 当前是第几页
     */
    private long current;
}
