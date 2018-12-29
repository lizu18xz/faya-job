package com.fayayo.job.core.executor.handler;

import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.core.log.LogContextHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc 所有执行器任务都继承此抽象类
 */
@Slf4j
public abstract class JobExecutorHandler {

    public void init() {
        log.info("init JobExecutorHandler");
    }


    public abstract Result<?> run(JobInfoParam jobInfoParam);


    public Result<?> execute(JobInfoParam jobInfoParam) {

        init();

        Result<?> result = run(jobInfoParam);

        post();

        return result;
    }

    public void post() {

        LogContextHolder.remove();

        log.info("end JobExecutorHandler");
    }
}
