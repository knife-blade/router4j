package com.knife.router4j.server.business.instance.service;

import com.knife.router4j.server.business.instance.request.InstanceReq;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;

import java.util.List;

public interface InstanceService {
    List<String> findAllApplicationNames(String applicationName);

    PageResponse<InstanceVO> findDefaultInstancePage(String applicationName,
                                                     PageRequest pageRequest);

    void setupDefaultInstance(List<InstanceReq> instanceReqs);
}
