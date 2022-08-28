package com.knife.router4j.common.entity;

import lombok.Data;

@Data
public class RuleInfo {
    private String applicationName;

    private String instanceAddress;

    private String pathPattern;
}
