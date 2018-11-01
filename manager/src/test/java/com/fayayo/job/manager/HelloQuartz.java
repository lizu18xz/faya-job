package com.fayayo.job.manager;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dalizu on 2018/11/1.
 * @version v1.0
 * @desc
 */
public class HelloQuartz implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        String name = detail.getJobDataMap().getString("name");

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("==============>测试 say hello to " + name + " at " + simpleDateFormat.format(new Date()).toString());
    }

}
