### faya-job
- 开发中经常会用到任务调度，为了学习,自己准备实现一个任务调度的系统，后续会配合页面。
- 将服务部署在分布式环境下的不同节点上，通过服务注册的方式，让客户端来自动发现当前可用的服务，并调用这些服务。这需要一种服务注册表（Service Registry）的组件，让它来注册分布式环境下所有的服务地址（包括：主机名与端口号）。
也可以手动配置服务的节点信息
### 参考学习:
- https://www.jianshu.com/p/634d2a6fae7b  quartz任务misfire策略
- 微博开源框架Motan
- 开源任务调度系统xxl-job

### 开发中 欢迎加入QQ群进行技术讨论 QQ:854622503

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
- 和springboot 进行整合  用户任务的调度

### zk 整合 进行服务注册和发现
- auto configureation
- 服务端启动后创建zkClient   客户端启动后也需要创建zkClient
- 注册服务  发现服务  监听服务的注册

### netty服务端客户端
- 使用Netty实现RPC的通信
- 负载均衡 部分代码来源于Motan
- ha的策略 部分代码来源于Motan

### 业务表设计
- 任务流信息
- 任务详细信息

### 整合新的执行器
- 可以参考默认的datax-executor 基于springboot
````
参数讲解:
xxx-executor:
     server: 127.0.0.1  服务地址
     port: 8888         服务端口
     weight: 1          服务的权重（负载均衡策略)
     name: datax        服务名称  自动注册执行器的时候使用
     mainClass: com.fayayo.job.datax.executor.DataxEngine  服务运行类,RPC通信的时候使用
````

### 使用详解
- 配置执行器
````
首先在管理端配置执行器（处理具体业务逻辑对应表 faya_job_group）,执行器名称对应执行器配置文件中的name
会根据执行器的名称注册到注册中心ZK
````

- 配置任务详细信息
````
选择执行器，然后配置任务的运行参数

````

### 部署方式


### 前端页面
- 执行器列表
- 执行器下面具体任务列表

### 接口测试(暂时)
````
新增任务POST:localhost:8081/manager/job/add?jobGroup=1&cron=*/10 * * * * ? &jobDesc=mysqlToMysql数据交换&jobType=BEAN&jobLoadBalance=3&jobHa=2&executorType=DATAX
````




### 后续规划
- 任务流组件，任务前后依赖，依次执行







