package com.suchtool.router4j.server.business.instance.service;

import com.suchtool.router4j.server.business.instance.request.DefaultInstanceRequest;
import com.suchtool.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.suchtool.router4j.server.business.instance.request.InstanceRequest;
import com.suchtool.router4j.server.business.instance.vo.InstanceForHeaderVO;
import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;

import java.util.List;

public interface InstanceService {
    InstanceForHeaderVO findAllInstance(InstanceRequest instanceRequest);

    Router4jPageVO<DefaultInstanceVO> findDefaultInstancePage(InstanceRequest instanceRequest, Router4jPageBO router4jPageBO);

    void setupDefaultInstance(List<DefaultInstanceRequest> defaultInstanceRequests);
}
