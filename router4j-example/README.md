router4j的示例程序。包含：注册中心、配置中心、业务程序。

测试通过的版本：
nacos-server-1.4.3

业务程序
order调用storage减库存，order再调用account减余额。