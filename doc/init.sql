
-- 数据库设计

-- 管理端
CREATE database if NOT EXISTS faya_job_manager default character set utf8mb4 collate utf8mb4_unicode_ci;
use faya_job_manager;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `telephone` varchar(30) DEFAULT NULL,
  `mail` varchar(50) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `operator` varchar(50) NOT NULL,
  `operate_ip` varchar(20) NOT NULL,
   create_time timestamp not null default current_timestamp comment '创建时间',
   update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`) USING BTREE,
  UNIQUE KEY `idx_mail` (`mail`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 执行器
drop table if exists faya_job_group;
create table faya_job_group(
  id int not null auto_increment,
  name varchar(64) not null comment '执行器名称',
  group_desc varchar(256) not null comment '执行器描述',
  seq int(11) NOT NULL DEFAULT '0' COMMENT '执行器展示的的顺序，由小到大',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id)
)comment '任务组信息表';


-- 任务流
drop table if exists faya_job_flow;
create table faya_job_flow(
  id varchar(32) not null comment '主键',
  name varchar(64) not null comment '任务流名称',
  flow_desc varchar(256) not null comment '任务流描述',
  seq int(11) NOT NULL DEFAULT '0' COMMENT '任务流展示的的顺序，由小到大',
  job_cycle int not null comment '任务执行周期 0-一次性 1-分钟任务 2-小时任务 3-天任务 4-周任务 5-月任务 6-cron自定义任务',
  job_cycle_value int not null comment '执行周期值',
  flow_priority int not null comment '任务流的优先级',
  flow_status int not null comment '任务流状态,上线,下线,删除',
  start_at datetime comment '任务开始时间',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id)
)comment '任务流信息表';



-- 任务信息表 job_info
-- alert table faya_job_info add job_cycle int not null comment '任务执行周期 0-一次性 1-分钟任务 2-小时任务 3-天任务 4-周任务 5-月任务 6-cron自定义任务';
-- alert table faya_job_info add job_flow int not null comment '所属任务流';
drop table if exists faya_job_info;
create table faya_job_info(
  id varchar(32) not null comment '主键',
  job_flow varchar(32) not null comment '所属任务流',
  job_group int not null comment '所属执行器',
  cron varchar(64) DEFAULT null comment '任务执行的cron表达式',
  job_desc varchar(256) not null comment '任务描述',
  executor_type varchar(32) not null comment '执行器类型',
  job_type varchar(16) not null comment '任务类型(result,...)',
  job_load_balance int not null comment '任务执行策略',
  job_ha int not null comment '任务ha的策略',
  retries int not null DEFAULT '0' comment '任务重试次数',
  job_status int not null comment '任务状态,上线,下线,删除',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id)
)comment '任务信息表';


-- 任务需要的额外配置文件内容
drop table if exists faya_job_config;
create table faya_job_config(
  job_id varchar(32) not null comment '任务唯一标示',
  content text not null comment '执行器描述',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (job_id)
)comment '任务配置文件信息';


-- 任务执行记录表 faya_job_log
drop table if exists faya_job_log;
create table faya_job_log(
  id varchar(32) not null comment '日志id',
  job_id varchar(32) not null comment '任务id',
  job_desc varchar(256) not null comment '任务描述',
  remote_ip varchar(32) not null comment '任务执行的机器地址',
  load_balance varchar(32) not null comment'负载策略',
  ha varchar(32) not null comment'失败策略',
  status int  comment '任务执行状态 成功 失败',
  retry int  comment '重试次数',
  message VARCHAR(128) comment '任务执行信息',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id)
)comment '任务日志';


-- 初始化数据
INSERT INTO `faya_job_manager`.`user` (`id`, `username`, `telephone`, `mail`, `password`, `operator`, `operate_ip`, `create_time`, `update_time`) VALUES ('1', 'admin', '15951610000', '535733495@qq.com', '123456', 'admin', '10.10.10.110', '2018-09-07 11:11:56', '2018-09-07 11:11:56');
