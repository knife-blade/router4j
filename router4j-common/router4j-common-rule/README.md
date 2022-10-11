### 说明
规则管理。作用有：
1. 设置路径匹配规则
2. 对请求的url重新匹配实例

### 配置示例
application.yml

````bash
router4j:
  enable: true
  redis:
    host: localhost
    password: 222333
    port: 6379
    # database: 0
    # ssl: false
    # timeout: 3s
  rule:
    # pathPatternPrefix: "router4j:rule:pathPattern"
    # defaultInstancePrefix: "router4j:rule:defaultInstance"
  # instruction:
  #   cacheKey: "router4j:instruction"
````
### 底层数据存放格式
#### 规则配置
router4j:rule:path-pattern:服务名:ip:端口=路径（列表）
#### 默认实例
router4j:rule:default-instance:服务名:ip:端口=是否强制路由（true/false）
- value的含义：在规则配置（router4j:rule:path-pattern）中已有配置时，是否强制路由到默认实例。


