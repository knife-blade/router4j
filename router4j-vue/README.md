# router4j-vue

## 项目描述
router4j服务端的前端项目

## 本项目打包后放到SpringBoot服务端运行

单独运行和打包运行要修改的文件是：

**单独运行**
- src/utils/request.js：修改getBaseUrl方法，返回此值：process.env.VUE_APP_BASE_API
- .env：修改VUE_APP_BASE_API，改为'/api'

**打包运行**
- src/utils/request.js：修改getBaseUrl方法，返回此值：location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '') + '/';
- .env：修改VUE_APP_BASE_API，改为''
