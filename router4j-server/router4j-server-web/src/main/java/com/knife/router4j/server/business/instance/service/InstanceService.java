package com.knife.router4j.server.business.instance.service;

import com.knife.router4j.server.business.instance.vo.InstanceVO;

import java.util.List;

public interface InstanceService {
    List<InstanceVO> findInstances();
}
