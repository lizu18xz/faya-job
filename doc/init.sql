
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


-- 任务组
drop table if exists faya_job_group;
create table faya_job_group(
  id int not null auto_increment,
  name varchar(64) not null comment '执行器名称',
  group_desc varchar(256) not null comment '执行器描述',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id)
)comment '任务信息表';


-- 任务信息表 job_info
drop table if exists faya_job_info;
create table faya_job_info(
  id int not null auto_increment,
  job_group int not null comment '所属执行器',
  cron varchar(64) not null comment '任务执行的cron表达式',
  job_desc varchar(256) not null comment '任务描述',
  executor_type varchar(32) not null comment '执行器类型',
  job_type varchar(16) not null comment '任务类型(bean,...)',
  job_load_balance int not null comment '任务执行策略',
  job_ha int not null comment '任务ha的策略',
  job_status int not null comment '任务状态,上线,下线,删除',
  start_at datetime comment '第一次任务开始时间',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id)
)comment '任务信息表';

-- 任务执行记录表 job_log



-- 初始化数据
INSERT INTO `faya_job_manager`.`user` (`id`, `username`, `telephone`, `mail`, `password`, `operator`, `operate_ip`, `create_time`, `update_time`) VALUES ('1', 'admin', '15951610000', '535733495@qq.com', '123456', 'admin', '10.10.10.110', '2018-09-07 11:11:56', '2018-09-07 11:11:56');
