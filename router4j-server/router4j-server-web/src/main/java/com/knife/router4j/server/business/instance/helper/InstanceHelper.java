package com.knife.router4j.server.business.instance.helper;

import com.knife.router4j.common.entity.DefaultInstanceRequest;
import com.knife.router4j.server.business.instance.request.InstanceReq;

import java.util.ArrayList;
import java.util.List;

public class InstanceHelper {
    public static List<DefaultInstanceRequest> toDefaultInstanceRequest(
            List<InstanceReq> instanceReqs) {
        List<DefaultInstanceRequest> requests = new ArrayList<>();

        for (InstanceReq addReq : instanceReqs) {
            DefaultInstanceRequest defaultInstanceRequest = new DefaultInstanceRequest();
            defaultInstanceRequest.setServiceName(addReq.getServiceName());
            defaultInstanceRequest.setInstanceAddress(addReq.getInstanceAddress());
            requests.add(defaultInstanceRequest);
        }
        return requests;
    }
}
