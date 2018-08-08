package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.manager.jobbean.RpcJobBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc quartz定时任务的核心处理类
 */
@Slf4j
@Service
public class JobSchedulerCore {

    @Autowired
    private Scheduler scheduler;

     /**
       *@描述 新增任务到quartz调度  name+group才是组成一个唯一key，通过key可以更新、停止任务等等。

       *@参数  任务id,任务组,任务调度表达式
     */
    public void addJob(String jobName,String jobGroup,String cron){

        try {
            //TODO 判断是否重复添加job

            JobKey jobKey = new JobKey(jobName, jobGroup);
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

            //加入job到quartz
            JobDetail jobDetail= JobBuilder.newJob(RpcJobBean.class)
                    .withIdentity(jobName,jobGroup)
                    //.usingJobData()  传递参数
                    .build();
            CronTrigger trigger = (CronTrigger) TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobName,jobGroup)
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule(cron)
                    ).build();
            try {
                scheduler.start();
                scheduler.scheduleJob(jobDetail,trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
                log.error("创建调度任务失败");
            }
        }catch (Exception e){
            throw new CommonException(ResultEnum.CREATE_SCHEDULE_ERROR);
        }
    }

   /* TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
    JobKey jobKey = new JobKey(jobName, jobGroup);
    // TriggerKey valid if_exists
        if (checkExists(jobName, jobGroup)) {
        logger.info(">>>>>>>>> addJob fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, jobName);
        return false;
    }
        logger.info(">>>>>>>>> addJob cronScheduleBuilder,{},{}", jobName, jobGroup);
    // CronTrigger : TriggerKey + cronExpression	// withMisfireHandlingInstructionDoNothing 忽略掉调度终止过程中忽略的调度
    CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
    CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
    Class<? extends Job> jobClass_ = RemoteHttpJobBean.class;   // Class.forName(jobInfo.getJobClass());
    JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();*/


   /* public static boolean checkExists(String jobName, String jobGroup) throws SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }*/


}
