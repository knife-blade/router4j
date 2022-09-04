package com.knife.router4j.server.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    /**
     * 每页的大小
     */
    private long size;

    /**
     * 当前是第几页
     */
    private long current;

    /**
     * 总的数据个数
     */
    private long total;

    /**
     * 数据列表
     */
    private List<T> dataList;
}
