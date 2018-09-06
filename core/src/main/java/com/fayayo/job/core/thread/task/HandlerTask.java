package com.fayayo.job.core.thread.task;

import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.executor.bean.Result;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;

import java.util.concurrent.Callable;

/**
 * @author dalizu on 2018/9/5.
 * @version v1.0
 * @desc 线程执行类
 */
public class HandlerTask implements Callable<Result<?>> {

    private JobExecutorHandler handler;

    private JobInfoParam jobInfo;

    public HandlerTask(JobExecutorHandler handler, JobInfoParam jobInfo) {
        this.handler = handler;
        this.jobInfo = jobInfo;
    }

    @Override
    public Result<?> call() throws Exception {

        handler.init();

        return handler.run(jobInfo);

    }

}
