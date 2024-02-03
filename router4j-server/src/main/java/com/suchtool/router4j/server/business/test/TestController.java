package com.suchtool.router4j.server.business.test;

import com.suchtool.nacosopenapi.api.NacosOpenApiTokenUtil;
import com.suchtool.nacosopenapi.api.NacosOpenApiUtil;
import com.suchtool.nacosopenapi.api.bo.NacosInstancePageBO;
import com.suchtool.nacosopenapi.api.bo.NacosLoginBO;
import com.suchtool.nacosopenapi.api.bo.NacosServicePageBO;
import com.suchtool.nacosopenapi.api.vo.NacosInstanceVO;
import com.suchtool.nacosopenapi.api.vo.NacosLoginVO;
import com.suchtool.nacosopenapi.api.vo.NacosNamespaceVO;
import com.suchtool.nacosopenapi.api.vo.NacosServiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private NacosOpenApiUtil nacosOpenApiUtil;

    @Autowired
    private NacosOpenApiTokenUtil nacosOpenApiTokenUtil;

    @PostMapping("login")
    public void login() {
        nacosOpenApiTokenUtil.createToken();
    }

    @PostMapping("queryAllNamespace")
    public List<NacosNamespaceVO> queryAllNamespace() {
        return nacosOpenApiUtil.queryAllNamespace();
    }

    @PostMapping("queryService")
    public List<NacosServiceVO> queryService(@RequestBody NacosServicePageBO nacosServicePageBO) {
        return nacosOpenApiUtil.queryService(nacosServicePageBO);
    }

    @PostMapping("queryInstance")
    public List<NacosInstanceVO> queryInstance(@RequestBody NacosInstancePageBO nacosInstancePageBO) {
        return nacosOpenApiUtil.queryInstance(nacosInstancePageBO);
    }
}
