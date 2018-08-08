
-- 数据库设计

-- 管理端
CREATE database if NOT EXISTS faya_job_manager default character set utf8mb4 collate utf8mb4_unicode_ci;
use faya_job_manager;


-- 任务信息表 job_info
drop table if exists faya_job_info;
create table faya_job_info(
  id int not null auto_increment,
  job_group int not null comment '所属执行器',
  cron varchar(64) not null comment '任务执行的cron表达式',
  job_desc varchar(256) not null comment '任务描述',
  job_type varchar(16) not null comment '任务类型',
  start_at datetime comment '任务开始时间',
  end_at datetime comment '任务结束时间',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id)
)comment '任务信息表';
