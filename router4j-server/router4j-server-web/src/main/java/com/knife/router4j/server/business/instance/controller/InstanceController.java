package com.knife.router4j.server.business.instance.controller;

import com.knife.router4j.common.entity.DefaultInstanceRequest;
import com.knife.router4j.common.util.DefaultInstanceUtil;
import com.knife.router4j.server.business.application.service.ApplicationService;
import com.knife.router4j.server.business.instance.helper.InstanceHelper;
import com.knife.router4j.server.business.instance.request.InstanceReq;
import com.knife.router4j.server.business.instance.vo.InstanceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "实例管理")
@RestController
@RequestMapping("instance")
public class InstanceController {
    @Autowired
    private DefaultInstanceUtil defaultInstanceUtil;

    @ApiOperation("设置为默认实例")
    @PostMapping("markAsDefaultInstance")
    public void markAsDefaultInstance(@RequestBody List<InstanceReq> addReqs) {
        List<DefaultInstanceRequest> requests =
                InstanceHelper.toDefaultInstanceRequest(addReqs);
        defaultInstanceUtil.markAsDefaultInstance(requests);
    }

    @ApiOperation("取消默认实例设置")
    @PostMapping("cancelDefaultInstance")
    public void cancelDefaultInstance(@RequestBody List<InstanceReq> deleteReqs) {
        List<DefaultInstanceRequest> requests =
                InstanceHelper.toDefaultInstanceRequest(deleteReqs);
        defaultInstanceUtil.cancelDefaultInstance(requests);
    }

    @ApiOperation("查找所有实例")
    @PostMapping("findAllDefaultInstance")
    public List<InstanceVO> findAllDefaultInstance() {

    }
}
