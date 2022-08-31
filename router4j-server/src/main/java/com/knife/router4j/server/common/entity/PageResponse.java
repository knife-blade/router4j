package com.knife.router4j.server.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private long size;

    private long current;

    private long total;

    private List<T> data;
}
