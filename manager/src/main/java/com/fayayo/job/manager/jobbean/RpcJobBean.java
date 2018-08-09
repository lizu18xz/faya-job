package com.fayayo.job.manager.jobbean;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 *  具体执行的job,根据jobId获取job信息，进行RPC的请求
 * */
@Slf4j
public class RpcJobBean extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("RpcJobBean start......");

    }


}
