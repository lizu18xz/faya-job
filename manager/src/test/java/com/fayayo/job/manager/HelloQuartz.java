package com.fayayo.job.manager;

import org.quartz.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dalizu on 2018/11/1.
 * @version v1.0
 * @desc @DisallowConcurrentExecution  (测试:如果加此注解,虽然是1秒调度一次withIntervalInSeconds(1)，但是还是要一个任务一个任务执行，日志打印会是5s一次，
 *  一定要等上次任务执行完成，才会执行下次任务)
 */
@DisallowConcurrentExecution
public class HelloQuartz implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        String name = detail.getJobDataMap().getString("name");

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==============>测试 say hello to " + name + " at " + simpleDateFormat.format(new Date()).toString());
    }

}
