package com.knife.router4j.server.business.defaultInstance.service;

import com.knife.router4j.server.business.defaultInstance.request.InstanceReq;
import com.knife.router4j.server.business.defaultInstance.vo.DefaultInstanceVO;
import com.knife.router4j.server.business.defaultInstance.vo.InstanceVO;
import com.knife.router4j.server.common.entity.PageRequest;
import com.knife.router4j.server.common.entity.PageResponse;

import java.util.List;

public interface InstanceService {
    List<InstanceVO> findAllInstance(InstanceVO instanceVO);

    PageResponse<DefaultInstanceVO> findDefaultInstancePage(InstanceVO instanceVO, PageRequest pageRequest);

    void setupDefaultInstance(List<InstanceReq> instanceReqs);
}
