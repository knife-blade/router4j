package com.suchtool.router4j.server.business.instance.service;

import com.suchtool.router4j.server.business.instance.request.DefaultInstanceRequest;
import com.suchtool.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.suchtool.router4j.server.business.instance.request.InstanceRequest;
import com.suchtool.router4j.server.business.instance.vo.InstanceForHeaderVO;
import com.suchtool.router4j.server.common.entity.PageRequest;
import com.suchtool.router4j.server.common.entity.PageResponse;

import java.util.List;

public interface InstanceService {
    InstanceForHeaderVO findAllInstance(InstanceRequest instanceRequest);

    PageResponse<DefaultInstanceVO> findDefaultInstancePage(InstanceRequest instanceRequest, PageRequest pageRequest);

    void setupDefaultInstance(List<DefaultInstanceRequest> defaultInstanceRequests);
}
