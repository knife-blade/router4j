package com.knife.order.dynamic;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service

public class MyInterceptor implements RequestInterceptor {

    private static Map<String, String> routerMap = new HashMap<String, String>();

    public void apply(RequestTemplate template) {

        template.header("Content-Type", "application/json");

        String serviceName = template.feignTarget().name();

        Map<String, Collection<String>> map = template.queries();

        //根据请求参数进行路由获取，这个配置可以自己决定放在什么地方

        String target = routerMap.get(map.get("projectId").toArray()[0] + ":" + serviceName);

        if (target != null) {

            template.target(target);

        }

        System.out.println("这是自定义请求拦截器," + template.path());

    }