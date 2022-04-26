// package com.knife.gateway.dynamic;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.BeanUtils;
// import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
// import org.springframework.cloud.gateway.route.RouteDefinition;
// import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
// import org.springframework.context.ApplicationEventPublisher;
// import org.springframework.context.ApplicationEventPublisherAware;
// import org.springframework.stereotype.Component;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
//
// /**
//  * redis 保存路由信息，优先级比配置文件高
//  * @author Zou.LiPing
//  */
// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class RedisRouteDefinitionWriter implements
// 		RouteDefinitionRepository, ApplicationEventPublisherAware {
//
// 	private final RedisUtils redisUtils;
//
// 	private ApplicationEventPublisher publisher;
//
// 	@Override
// 	public Mono<Void> save(Mono<RouteDefinition> route) {
// 		return route.flatMap(r -> {
// 			RouteDefinitionVo vo = new RouteDefinitionVo();
// 			BeanUtils.copyProperties(r, vo);
// 			log.info("保存路由信息{}", vo);
// 			redisUtils.hPut(CacheConstants.ROUTE_KEY,r.getId(), JSON.toJSONString(vo));
// 			refreshRoutes();
// 			return Mono.empty();
// 		});
// 	}
//
// 	@Override
// 	public Mono<Void> delete(Mono<String> routeId) {
// 		routeId.subscribe(id -> {
// 			log.info("删除路由信息={}", id);
// 			redisUtils.hDelete(CacheConstants.ROUTE_KEY, id);
// 			refreshRoutes();
// 		});
// 		return Mono.empty();
// 	}
//
// 	private void refreshRoutes() {
// 		this.publisher.publishEvent(new RefreshRoutesEvent(this));
// 	}
//
// 	/**
// 	 * 动态路由入口
// 	 * @return Flux<RouteDefinition>
// 	 */
// 	@Override
// 	public Flux<RouteDefinition> getRouteDefinitions() {
//
// 		List<RouteDefinition> definitionList = new ArrayList<>();
// 		Map<Object, Object> objectMap = redisUtils.hGetAll(CacheConstants.ROUTE_KEY);
// 		if (Objects.nonNull(objectMap)) {
// 			for (Map.Entry<Object, Object> objectObjectEntry : objectMap.entrySet()) {
// 				RouteDefinition routeDefinition = JSON.parseObject(objectObjectEntry.getValue().toString(),RouteDefinition.class);
// 				definitionList.add(routeDefinition);
// 			}
// 		}
// 		log.info("redis 中路由定义条数： {}， definitionList={}", definitionList.size(), definitionList);
// 		return Flux.fromIterable(definitionList);
// 	}
//
// 	@Override
// 	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
// 		this.publisher = applicationEventPublisher;
// 	}
//
// }