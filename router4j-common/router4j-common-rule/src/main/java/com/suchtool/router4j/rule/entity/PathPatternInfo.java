package com.suchtool.router4j.rule.entity;

import lombok.Data;

@Data
public class PathPatternInfo {
    private String applicationName;

    private String instanceAddress;

    private String pathPattern;
}
