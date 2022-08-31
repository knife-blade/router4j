package com.knife.router4j.server.common.entity;

import lombok.Data;

@Data
public class PageRequest {
    private long size;

    private long current;
}
