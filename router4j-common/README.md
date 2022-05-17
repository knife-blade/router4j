# 说明
公共项目。作用有：
1. 路径匹配
2. 操作Redis

# 配置示例
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
    # prefix: "router4j:rule:prefix:"
````


