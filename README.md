### faya-job
- 开发中经常会用到任务调度，为了学习,自己准备实现一个任务调度的系统，后续会配合页面。
- 大体思路:
  - 将服务部署在分布式环境下的不同节点上，通过服务注册的方式，让客户端来自动发现当前可用的服务，并调用这些服务。
### 参考学习:
- https://www.jianshu.com/p/634d2a6fae7b  quartz任务misfire策略
- 微博开源框架Motan
- 开源任务调度系统xxl-job
- 学习的时候使用到了上面两个开源框架的思路和代码

### 整体项目流程
- 项目分为管理节点和工作节点
- 在管理节点可以配置任务信息
- 调度层使用quartz进行任务的调度
- 工作节点启动成功后,会向zk进行注册,可以启动多个工作节点
- 管理节点通过quartz进行调度的时候,通过会通过loadbalance策略选择合适的工作节点。
- 管理节点会通过代理类向选择的工作节点发送RPC请求
- 工作节点接收到请求后执行真正的调度业务

### 基础模块搭建
- 模块划分:
````
common:通用的方法工具
core:核心实现
entity:实体类相关
manager:管理端
demo-executor 执行器的demo
datax-executor 数据交换执行器
jar-executor  可执行jar执行器
````
###  调度核心 springboot+quartz整合
- 使用quartz的集群模式
- 和springboot 进行整合

###  注册中心  zk 进行服务注册和发现
- Auto Configureation
- 服务端启动后创建zkClient   客户端启动后也需要创建zkClient
- 注册服务  发现服务  监听服务的注册

### 高可用 客户端负载均衡
- 负载均衡策略
- ha的策略

###  transport层  netty实现服务端客户端
- 使用Netty实现RPC的通信


### 管理端部署
- mvn clean package -DskipTests  项目整体打包
- 启动项目的zk
- 启动管理端:java -jar FayaManager.jar
- 访问管理页面地址:http://localhost:8086(需要启动前端项目)
- 日志配置: 因为datax项目本身的日志规则不符合项目需求，所以修改启动脚本。
````
  进入datax/conf目录
  1-修改datax.py
  #jobParams = ("-Dlog.file.name=%s") % (jobResource[-20:].replace('/', '_').replace('.', '_'))
  jobParams = ("-Dlog.file.name=%s") % (jobResource[-24:].replace('/','_'))
  
  2-修改logback.xml,让日志根据日志ID来生成文件
  <!--  <file>${log.dir}/${ymd}/${log.file.name}-${byMillionSecond}.log</file> -->
        <file>${log.dir}/${ymd}/${log.file.name}.log</file>
````

### 如何整合新的执行器(基于springboot)?
- demo-executor作为基本的测试DEMO，可以直接扩展
- 也可以参考默认的datax-executor 基于springboot,用于实现数据交换

### datax-executor 参数详细信息
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

### 部署执行器方式
- DATAX执行器
  - 找到application.yml文件，检查配置文件或者修改为自己想要的地址和路径
  - 使用maven命令打包 mvn clean package -DskipTests
  - 部署jar包到服务器
  - 创建application.yml中配置的datax.config文件夹
  - 部署datax,设置datax环境变量DATAX_HOME
    - mvn -U clean package assembly:assembly -Dmaven.test.skip=true
    - export DATAX_HOME=/home/faya/datax/datax
      export PATH=$DATAX_HOME/bin:$PATH
      

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

### 前端页面(React)
- 执行器管理页面
- 执行器下面具体任务管理页面
- 运维中心  展示任务名称  和任务执行的日志  滚动
- 部署 yarn install
- 启动 yarn dev
### github地址:https://github.com/lizu18xz/admin-v1-fe.git


### 后续规划
- 完善日志
- 任务配置限制，分钟任务，小时任务，天任务，一次性任务
- 页面可配(数据源配置，任务详细信息配置)
- 任务流组件，任务前后依赖，依次执行

### 准备优化
````

整体流程的改变
1-任务流(后续 功能扩展 任务流中有一个或者多个任务，有依赖关系进行执行，暂时只包含一个任务   )
2-希望实现DAG图形，组织任务流。

新增任务种类:
Hive  sql 任务
sqoop 类似datax hive->   rdb  全表导出,海量数据（千万以上）执行效率快,不支持部分字段导出
Spark  Sql  任务主要  用于计算逻辑较复杂的业务。
````

### 常用的cron表达式
- */10 * * * * ?  每10秒钟
- 0 0/10 /1 * * ? 每十分钟
- 0 0 /1 * * ?    每小时
- 0 0 2 1/1 * ?   每日凌晨两点
- 0 */5 * * * ?   每隔5分执行一次：0 */5 * * * ?
- 0 0 1 * * ?     每天凌晨1点执行一次：0 0 1 * * ?
- 0 0 0/1 * * ?   每个整点执行一次：0 0 0/1 * * ?
- 0 0 7-23 * * ?  每天7点到23点，每整点执行一次：0 0 7-23 * * ?


### 开发中 欢迎加入QQ群进行技术讨论 QQ:854622503