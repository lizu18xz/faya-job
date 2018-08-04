### faya-job
- 将服务部署在分布式环境下的不同节点上，通过服务注册的方式，让客户端来自动发现当前可用的服务，并调用这些服务。这需要一种服务注册表（Service Registry）的组件，让它来注册分布式环境下所有的服务地址（包括：主机名与端口号）。
也可以手动配置服务的节点信息
- app ---> registerServer  ---多个server
### 基础框架搭建
- 模块划分:
````
common:通用的方法工具
core:核心实现
entity:实体类相关
manager:job管理端
datax-executor 数据交换执行器
jar-executor  可执行jar执行器
````
###  springboot quartz整合
- 使用quartz的集群模式
- 和springboot 进行整合

### zk 整合 进行服务注册和发现
- auto configureation
- 服务端启动后创建zkClient   客户端启动后也需要创建zkClient













































3-  netty整合  rpc
4-  表设计
4-  业务开发
6-  默认提供执行器是进行数据交换 或者 运行可执行jar的 
7-  可以扩展执行器，会新建监听的端口


### 开发中 欢迎加入QQ群进行技术讨论
### QQ:854622503
