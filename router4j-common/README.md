### 说明
公共项目。作用有：
1. 路径匹配
2. 操作Redis

### 配置示例
application.yml

````bash
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
````
### 底层数据存放格式
#### 规则数据
router4j:rule:pathPattern:服务名:ip:端口=路径（列表）

