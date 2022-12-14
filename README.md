# 前言
后端开发时如果同一个应用起了多个实例，会遇到以下问题：
1. 无法将指定url请求强制转到个人电脑。这样会导致难以打断点调试。如果是设置了应用的所有请求都到我的电脑，那么我在调试时其他接口无法使用。
2. 无法将指定的feign的url请求强制转到个人电脑。
3. 如果有人打了断点调试，很可能影响测试环境。因为网关的请求和feign请求还是会经常请求到打了断点的那个电脑。

本文介绍的router4j可以解决这个问题，是我个人写的中间件。它可以设置将指定的url的请求路由到指定的机器。

 **官网网址** 
- 官网文档：[router4j官网](https://www.yuque.com/knifeblade/opensource/router4j) （首先在官网更新，不定时同步到[CSDN博客](https://knife.blog.csdn.net/article/details/126930621 "CSDN博客")）
- github：https://github.com/knife-blade/router4j
- gitee：https://gitee.com/knifeedge/router4j

# 一、简介
## 1.1 概述
router4j是一个动态路由的平台，用于提高Java后端开发和调试效率。

使用router4j，可以将某个url请求路由到指定的机器上，只需在可视化页面上将path绑定到应用实例的ip和端口上即可。

router4j也支持将所有请求强制转到指定机器，所以也可以用于线上不停机更新应用。
## 1.2 特性
- 简单：支持通过Web页面对任务进行CRUD操作，操作简单，一分钟上手；
- 无侵入：引入依赖即可，无需修改代码
- 性能损耗小：路径的规则存放在Redis，速度很快，几乎没有性能损耗
- 支持路径的路由：可以指定某个url路由到指定机器
- 支持默认路由：如果url没有设置规则，则转发到设置的默认路由的实例
- 支持强制路由：所有url强制路由到指定实例（无论是否设置了规则）
- 支持网关：支持网关的请求转发
- 支持feign：支持feign的请求转发
- 支持实例运行状态查看：支持查看实例的运行状态

## 1.3 支持的框架
- 网关
	- gateway（已支持）
	- zuul（待支持）
- SpringCloud
	- loadbalancer（已支持）（SpringCloud2020及之后的版本）
	- ribbon（待支持）
	
## 1.4 框架结构
在可视化页面的配置会保存到Redis，请求进来时，会根据Redis中的配置路由到指定的实例。
## 1.5 下载
github：https://github.com/knife-blade/router4j

gitee：https://gitee.com/knifeedge/router4j

maven：（待上传）
# 二、快速入门
- router4j-server：router4j的控制台（后端）
- router4j-vue：router4j的控制台（前端）
- router4j-example：示例程序

## 2.1 启动控制台
**1.启动后端**

启动router4j-server

**2.启动前端**

启动router4j-vue

## 2.2 添加依赖
**1.网关添加依赖**
router4j-example/gateway
```xml
    <dependency>
        <groupId>com.knife.router4j</groupId>
        <artifactId>router4j-client-gateway-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```
	
**2.应用添加依赖（用于feign）**
router4j-example/business/order/order-core
```xml
<dependency>
  <groupId>com.knife.router4j</groupId>
  <artifactId>router4j-client-feign-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
## 2.3 配置
网关和应用都要添加如下配置：
```bash
router4j:
  redis:
    host: localhost
    password: 222333
    port: 6379
    # database: 0
    # ssl: false
    # timeout: 3s
  rule:
    enable: true
    # pathPatternPrefix: "router4j:rule:pathPattern"
    # defaultInstancePrefix: "router4j:rule:defaultInstance"
  # instruction:
  #   cacheKey: "router4j:instruction"
```
## 2.4 开始使用
- 启动router4j服务：Redis、router4j-server、router4j-vue
- 启动示例服务（router4j-example路径）：Nacos、geteway、业务应用（business路径）（2个order，2个storage，1个account）。
- 业务场景：创建订单，减库存、减账户余额。请求order的controller，order分别用feign调用storage和account。
- 需求：将网关请求order的/order/create转到192.168.150.1:9011这个实例，将storage的feign转到192.168.150.1:9021这个实例。

**1.配置网关路径的路由**

访问：http://localhost:10001/

在“规则管理”中配置路径与实例的绑定。

**2.配置feign路径的路由**

**3.请求接口测试**

postman访问（请求10次）：http://localhost:6001/order/order/create/?userId=1&productId=1&count=10&money=100

结果：
- order：（全部都请求到了指定的实例192.168.150.1:9012）
- storage：（全部都请求到了指定的实例192.168.150.1:9021）
