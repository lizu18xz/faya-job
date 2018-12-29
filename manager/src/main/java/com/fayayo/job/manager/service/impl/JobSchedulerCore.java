package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.CycleEnums;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.util.DateTimeUtil;
import com.fayayo.job.manager.core.helper.RpcJobHelper;
import com.fayayo.job.manager.core.jobbean.RpcJobBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.DisposableBean;
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
     * @描述 校验是否存在相同的任务
     */
    public boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }

    /**
     * @描述 从quartz中暂停某个任务
     * @创建人 dalizu
     * @创建时间 2018/8/9
     */
    public void pauseJob(String jobName, String jobGroup) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("暂停任务异常:{}", e);
            throw new CommonException(ResultEnum.PAUSE_SCHEDULE_ERROR);
        }
    }

    /**
     * @描述 从quartz中恢复暂停的任务
     * @创建人 dalizu
     * @创建时间 2018/8/9
     */
    public void resumeJob(String jobName, String jobGroup) {

        try {
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("恢复暂停任务异常:{}", e);
            throw new CommonException(ResultEnum.RESUME_SCHEDULE_ERROR);
        }
    }

    /**
     * @描述 从quartz修改一个任务的触发时间
     * @创建人 dalizu
     * @创建时间 2018/8/9
     */
    public void rescheduleJob(String jobName, String jobGroup, String cron) {

        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();

            if (!oldTime.equalsIgnoreCase(cron)) {
                trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("{}成功修改任务到调度中心");
            } else {
                log.info("{}调度时间没有修改");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("修改调度任务异常:{}", e);
        }

    }


    //任务流上线任务 TODO 是否等待上次完成，还是不等待直接执行这一次
    public void addFlowJob(String jobId, String jobGroup, Date time, Integer cycle,Integer cycleValue) {

        ScheduleBuilder scheduleBuilder = null;

        if (cycle.equals(CycleEnums.ONE.getCode())) {

            scheduleBuilder=SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(0)//只执行一次
                    .withMisfireHandlingInstructionFireNow();//忽略已经MisFire的任务，并且立即执行调度。这通常只适用于只执行一次的任务。

        } else if (cycle.equals(CycleEnums.MINUTE.getCode())) {

            scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                    .withIntervalInMinutes(cycleValue)
                    .withMisfireHandlingInstructionDoNothing();//所有的misfire不管，执行下一个周期的任务

        } else if (cycle.equals(CycleEnums.HOUR.getCode())) {

            scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                    .withIntervalInHours(cycleValue)
                    .withMisfireHandlingInstructionDoNothing();//所有的misfire不管，执行下一个周期的任务

        } else if (cycle.equals(CycleEnums.DAY.getCode())) {

            scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                    .withIntervalInDays(cycleValue)
                    .withMisfireHandlingInstructionDoNothing();//所有的misfire不管，执行下一个周期的任务

        } else if (cycle.equals(CycleEnums.WEEK.getCode())) {

            scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                    .withIntervalInWeeks(cycleValue)
                    .withMisfireHandlingInstructionDoNothing();//所有的misfire不管，执行下一个周期的任务

        } else if (cycle.equals(CycleEnums.MON.getCode())) {

            scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                    .withIntervalInMonths(cycleValue)
                    .withMisfireHandlingInstructionDoNothing();//所有的misfire不管，执行下一个周期的任务

        }else if (cycle.equals(CycleEnums.CRON.getCode())) {
            //TODO 暂时不支持
            scheduleBuilder = CronScheduleBuilder.cronSchedule("").//quartz提出了misfire的理论，让任务在错过之后，还能正常的运行。
                    withMisfireHandlingInstructionDoNothing();

        }

        scheduler(jobId, jobGroup, time, scheduleBuilder);

    }


    private void scheduler(String jobId, String jobGroup, Date time, ScheduleBuilder scheduleBuilder) {

        try {

            //判断是否重复添加job
            if (checkExists(jobId, jobGroup)) {
                log.info("addJob fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, jobId);
                throw new CommonException(ResultEnum.CREATE_SCHEDULE_ERROR);
            }

            JobKey jobKey = new JobKey(jobId, jobGroup);
            TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);

            JobDetail jobDetail = JobBuilder.newJob(RpcJobBean.class)
                    .withIdentity(jobKey)
                    .build();

            //定义一个Trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .startAt(time)
                    .withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder)
                    .build();

            try {
                scheduler.start();
                Date date = scheduler.scheduleJob(jobDetail, trigger);
                log.info("{}成功加入任务到调度中心-->jobName:{},jobGroup:{},任务开始启动时间:{}", Constants.LOG_PREFIX, jobId, jobGroup, DateTimeUtil.dateToStr(date));
            } catch (SchedulerException e) {
                e.printStackTrace();
                log.error("创建调度任务失败");
                throw new CommonException(ResultEnum.CREATE_SCHEDULE_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * @描述 从quartz中删除某个任务
     * @创建人 dalizu
     * @创建时间 2018/8/9
     */
    public void removeFlowJob(String jobName, String jobGroup) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("删除调度任务异常:{}", e);
            throw new CommonException(ResultEnum.REMOVE_SCHEDULE_ERROR);
        }
    }

}
