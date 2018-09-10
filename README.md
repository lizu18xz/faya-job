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

- 配置任务详细信息(暂时支持页面配置json文件，保存)
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

### DATAX特殊参数配置信息
````
core.json
1-core.container.taskGroup.channel
   1.1将拆分成的Task重新组合后 组装成的TaskGroup(任务组),默认启动的线程数量去执行这些Task
   1.2和job配置中的channel配合使用，可以提高taskGroup的并发个数(前提是没有配置byte和record)
每一个Task都由TaskGroup负责启动，Task启动后，会固定启动Reader—>Channel—>Writer的线程来完成任务同步工作。
2-core.transport.channel.speed.byte
   2.1首先作为单个channel的byte大小限速参数
   2.2和job中配置的job.setting.speed.byte配合使用，可以提高taskGroup的并发个数
3-core.transport.channel.speed.record
   3.1首先作为单个channel的record数量限速参数
   3.2和job中配置的job.setting.speed.record配合使用，可以提高taskGroup的并发个数
job.json
1-job.setting.speed.channel
  1.1job.setting.speed.channel/core.container.taskGroup.channel  获取taskGroup并发的个数
2-job.setting.speed.byte
  1.1job.setting.speed.byte/core.transport.channel.speed.byte)/core.container.taskGroup.channel 获取taskGroup并发的个数
3-job.setting.speed.record
  1.1job.setting.speed.record/core.transport.channel.speed.record)/core.container.taskGroup.channel 获取taskGroup并发的个数
  
实例:
全局限速:
job.setting.speed.byte  1000  可以理解为整个任务所有任务的速度总和
core.transport.channel.speed.byte 100 单个channel限速
core.container.taskGroup.channel 5

此时如果切分后task的个数大于通过byte计算出的needChannelNumber，
可以获取到taskGroup的个数为10/5=2
每个taskGroup提交到固定大小为2的线程池执行任务,并且执行任务的并发数是5


  
````







### 后续规划
- 页面可配(数据源配置，任务详细信息配置)
- 任务流组件，任务前后依赖，依次执行







