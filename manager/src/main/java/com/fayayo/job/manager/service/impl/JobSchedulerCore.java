package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.util.DateTimeUtil;
import com.fayayo.job.manager.jobbean.RpcJobBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public void addJob(String jobId,String jobGroup,String cron,Date startAt){

        try {
            //判断是否重复添加job
            if (checkExists(jobId, jobGroup)) {
                log.info("addJob fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, jobId);
                throw new CommonException(ResultEnum.CREATE_SCHEDULE_ERROR);
            }

            JobKey jobKey = new JobKey(jobId, jobGroup);
            TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);

            //加入job到quartz
            JobDetail jobDetail= JobBuilder.newJob(RpcJobBean.class)
                    .withIdentity(jobKey)
                    .build();

            CronScheduleBuilder cronScheduleBuilder=CronScheduleBuilder.cronSchedule(cron).//quartz提出了misfire的理论，让任务在错过之后，还能正常的运行。
                    withMisfireHandlingInstructionDoNothing();//所有的misfire不管，执行下一个周期的任务

            CronTrigger trigger=null;
            if(startAt!=null){
                 trigger = (CronTrigger) TriggerBuilder
                        .newTrigger()
                        .startAt(startAt)      //开始时间
                        .withIdentity(triggerKey)
                        .withSchedule(cronScheduleBuilder).build();
            }else {
                 trigger = (CronTrigger) TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(cronScheduleBuilder).build();
            }
            try {
                scheduler.start();
                Date date=scheduler.scheduleJob(jobDetail,trigger);
                log.info("成功加入任务到调度中心-->jobName:{},jobGroup:{},任务开始启动时间:{}",jobId,jobGroup,DateTimeUtil.dateToStr(date));
            } catch (SchedulerException e) {
                e.printStackTrace();
                log.error("创建调度任务失败");
            }
        }catch (Exception e){
            throw new CommonException(ResultEnum.CREATE_SCHEDULE_ERROR);
        }
    }


    /**
      *@描述 校验是否存在相同的任务
    */
   public boolean checkExists(String jobName, String jobGroup) throws SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }


    /**

     *@描述 从quartz中删除某个任务

     *@创建人  dalizu

     *@创建时间  2018/8/9

     */
    public void removeJob(){

    }







}
