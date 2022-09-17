/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.knife.router4j.feign.config.loadBalancer;

import com.knife.router4j.common.common.entity.InstanceInfo;
import com.knife.router4j.common.util.ClientPathRuleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 根据Redis配置路由到某个实例
 */
@Slf4j
public class Router4jLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final String serviceId;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    /**
     * @param serviceInstanceListSupplierProvider a provider of
     *                                            {@link ServiceInstanceListSupplier} that will be used to get available instances
     * @param serviceId                           id of the service for which to choose an instance
     */
    public Router4jLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Autowired
    private ClientPathRuleUtil clientPathRuleUtil;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);

        return supplier.get(request).next()
                .map(serviceInstances -> getInstanceResponse(request, serviceInstances));
    }

    private Response<ServiceInstance> getInstanceResponse(Request request,
                                                          List<ServiceInstance> serviceInstances) {
        if (serviceInstances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }

        RequestDataContext requestDataContext = (RequestDataContext) (request.getContext());
        URI url = requestDataContext.getClientRequest().getUrl();
        String path = url.getPath();

        // 去Redis查找匹配的实例
        InstanceInfo matchedInstance = clientPathRuleUtil.findMatchedInstance(serviceId, path);
        if (matchedInstance == null) {
            return selectRandomInstance(serviceInstances);
        }

        // 将特定应用的请求路由到指定实例
        String host = matchedInstance.getHost();
        int port = matchedInstance.getPort();

        ServiceInstance instanceResult = null;
        for (ServiceInstance instance : serviceInstances) {
            if (host.equals(instance.getHost())
                    && port == instance.getPort()) {
                instanceResult = instance;
                return new DefaultResponse(instanceResult);
            }
        }

        // 如果指定的实例不可用，则随机取一个可用的服务
        return selectRandomInstance(serviceInstances);
    }

    private Response<ServiceInstance> selectRandomInstance(List<ServiceInstance> serviceInstances) {
        int index = ThreadLocalRandom.current().nextInt(serviceInstances.size());
        ServiceInstance instanceResult = serviceInstances.get(index);
        return new DefaultResponse(instanceResult);
    }

}
