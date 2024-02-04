package com.suchtool.router4j.common.instance.applicationInfo.bo;

import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InstancePageBO extends Router4jPageBO {
    private String namespaceName;

    private String applicationName;
}
