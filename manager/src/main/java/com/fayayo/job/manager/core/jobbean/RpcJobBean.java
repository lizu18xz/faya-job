package com.fayayo.job.manager.core.jobbean;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.manager.core.helper.RpcJobHelper;
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

        //TODO 修改任务运行状态

        //获取任务的jobKey和groupId
        String jobId=jobExecutionContext.getJobDetail().getKey().getName();
        log.info("{}start to execute job key：{}",Constants.LOG_PREFIX,jobId);

        //执行具体的业务逻辑  发送rpc请求
        RpcJobHelper.getInstance().addJobInPool(jobId);

    }


}
