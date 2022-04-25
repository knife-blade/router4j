package com.knife.router4j.business.application.util;

import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.core.HostReactor;
import com.knife.router4j.common.util.ApplicationContextHolder;
import com.knife.router4j.common.util.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class NacosUtil {

    private static NacosNamingService namingService;

    private HostReactor hostReactor;

    @SneakyThrows
    public static void onEvent(Event event) {
        log.info("nacos监听到事件消息:{}", JsonUtil.getStringFromObject(event));

        namingService = ApplicationContextHolder.getContext().getBean(NacosNamingService.class);
        Class<?> cls = Class.forName("com.alibaba.nacos.client.naming.NacosNamingService");
        Field field = cls.getDeclaredField("hostReactor");
        field.setAccessible(true);
        Object hostReactorValue = field.get(namingService);
        // this.hostReactor = (HostReactor) hostReactorValue;
        // this.hostReactor.updateServiceNow("服务名", "");

    }
}
