// package com.knife.order.dynamic;
//
// import feign.RequestInterceptor;
// import feign.RequestTemplate;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// import java.util.Collection;
// import java.util.HashMap;
// import java.util.Map;
//
// /**
//  * 此法无法指定域名、端口号。放弃
//  */
// @Configuration
// public class Router4jClientInterceptorConfig {
//     @Bean
//     public RequestInterceptor router4jClientInterceptor() {
//         return new Router4jClientInterceptor();
//     }
//
//     public static class Router4jClientInterceptor implements RequestInterceptor {
//         @Override
//         public void apply(RequestTemplate template) {
//             // 可设置/读取 header信息
//             // template.header("Content-Type", "application/json");
//
//             // 可读取url上的参数
//             Map<String, Collection<String>> map = template.queries();
//
//             // 可读取路径。例如：/feign/storage/decreaseStorage
//             String path = template.path();
//
//             // 可获得服务名。例：storage
//             String serviceName = template.feignTarget().name();
//         }
//     }
// }
