package com.suchtool.router4j.server.business.instance.service;

import com.suchtool.router4j.server.business.instance.bo.DefaultInstanceBO;
import com.suchtool.router4j.server.business.instance.vo.DefaultInstanceVO;
import com.suchtool.router4j.server.business.instance.bo.InstanceBO;
import com.suchtool.router4j.server.business.instance.vo.InstanceForHeaderVO;
import com.suchtool.router4j.common.common.entity.Router4jPageBO;
import com.suchtool.router4j.common.common.entity.Router4jPageVO;

import java.util.List;

public interface InstanceService {
    InstanceForHeaderVO findAllInstance(InstanceBO instanceBO);

    Router4jPageVO<DefaultInstanceVO> findDefaultInstancePage(InstanceBO instanceBO, Router4jPageBO router4jPageBO);

    void setupDefaultInstance(List<DefaultInstanceBO> defaultInstanceBOS);
}
