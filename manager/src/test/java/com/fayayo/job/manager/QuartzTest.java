package com.fayayo.job.manager;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author dalizu on 2018/11/1.
 * @version v1.0
 * @desc
 */
public class QuartzTest {

    public static void main(String[] args) throws ParseException {

        min();//分钟任务

        //hour();//小时任务

        //day();

        //cron();

    }
    //CalendarIntervalScheduleBuilder.calendarIntervalSchedule()  intervalUnit 执行间隔的单位（秒，分钟，小时，天，月，年，星期）

    private static void min() throws ParseException {

        //指定开始时间
        String time="2018-11-01 20:02:01";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //创建scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //定义一个Trigger
            Trigger trigger = newTrigger().withIdentity("trigger1", "group1")
                    //.startNow()
                    .startAt(simpleDateFormat.parse(time))
                    .withSchedule(calendarIntervalSchedule()
                            .withIntervalInMinutes(1))
                    .build();



            JobDetail job = newJob(HelloQuartz.class)
                    .withIdentity("job1", "group1")
                    .usingJobData("name", "quartz")
                    .build();

            scheduler.scheduleJob(job, trigger);

            scheduler.start();

            /*//运行一段时间后关闭
            Thread.sleep(10000);
            scheduler.shutdown(true);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private static void hour(){
        try {
            //创建scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //定义一个Trigger
            Trigger trigger = newTrigger().withIdentity("trigger1", "group1") //定义name/group
                    .startNow()                    //一旦加入scheduler，立即生效
                    .withSchedule(calendarIntervalSchedule() //使用SimpleTrigger
                            .withIntervalInHours(1)) //每隔一小时钟执行一次
                    .build();

            JobDetail job = newJob(HelloQuartz.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
                    .withIdentity("job1", "group1") //定义name/group
                    .usingJobData("name", "quartz") //定义属性
                    .build();

            scheduler.scheduleJob(job, trigger);

            scheduler.start();

            /*//运行一段时间后关闭
            Thread.sleep(10000);
            scheduler.shutdown(true);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void day(){

        try {
            //创建scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //定义一个Trigger
            Trigger trigger = newTrigger().withIdentity("trigger1", "group1") //定义name/group
                    .startNow()                    //一旦加入scheduler，立即生效
                    .withSchedule(calendarIntervalSchedule()//使用SimpleTrigger
                           .withIntervalInDays(1))
                          .build();

            JobDetail job = newJob(HelloQuartz.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
                    .withIdentity("job1", "group1") //定义name/group
                    .usingJobData("name", "quartz") //定义属性
                    .build();

            scheduler.scheduleJob(job, trigger);

            scheduler.start();

            /*//运行一段时间后关闭
            Thread.sleep(10000);
            scheduler.shutdown(true);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void cron(){

        try {
            //创建scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //定义一个Trigger
            Trigger trigger = newTrigger().withIdentity("trigger1", "group1") //定义name/group
                    .startNow()                    //一旦加入scheduler，立即生效
                    .withSchedule(cronSchedule("0 0/1 * * * ?"))
                    .build();

            JobDetail job = newJob(HelloQuartz.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
                    .withIdentity("job1", "group1") //定义name/group
                    .usingJobData("name", "quartz") //定义属性
                    .build();

            scheduler.scheduleJob(job, trigger);

            scheduler.start();

            /*//运行一段时间后关闭
            Thread.sleep(10000);
            scheduler.shutdown(true);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
