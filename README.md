# router4j

#### 介绍
本项目是网关层的动态路由。在网关引入本项目依赖，然后在页面中指定某个url路由到某个ip，请求进来时，网关会按照设置好的规则路由到指定ip。

#### 各项目的作用


- router4j-client：用在客户端（网关） 
- router4j-server：路由服务端，提供前端页面和规则的配置功能。（router4j-vue打包+规则配置后端接口） 
- router4j-vue：路由服务端的前端代码。 
- router4j-example：示例代码。包含：网关、注册中心、业务代码、对router4j-client的使用。
 
