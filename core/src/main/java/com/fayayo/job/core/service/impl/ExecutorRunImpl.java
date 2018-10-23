package com.fayayo.job.core.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.JobTypeEnums;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.executor.JobExecutor;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import com.fayayo.job.core.thread.StandardThreadExecutor;
import com.fayayo.job.core.thread.StandardThreadManager;
import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.core.callback.CallBackParam;
import com.fayayo.job.core.callback.CallbackThread;
import com.fayayo.job.core.callback.task.HandlerTask;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Future;

/**
 * @author dalizu on 2018/8/18.
 * @version v1.0
 * @desc 具体执行任务的类
 */
@Slf4j
public class ExecutorRunImpl implements ExecutorRun {

    public static StandardThreadExecutor futureThread= StandardThreadManager.futureThread();

    private String server;

    public ExecutorRunImpl(String server) {
        this.server = server;
    }


     /**
       *@描述 真正执行业务逻辑的地方,在rpc server里面通过反射调用
     */
    @Override
    public Result<?> run(JobInfoParam jobInfo) {

        log.info("{}Start to Run Job,执行器类型:{},Params:{}", Constants.LOG_PREFIX,jobInfo.getExecutorType(),
                jobInfo.toString());

        String type=jobInfo.getJobType();
        JobExecutorHandler handler=null;
        if(type.equalsIgnoreCase(JobTypeEnums.BEAN.getName())){
            //获取具体执行的服务
            handler=JobExecutor.getHandler();
        }else {
            log.info("{}暂未开放其他类型处理器!!!!!!",Constants.LOG_PREFIX);
        }

        //异步处理任务
        Future<?>future= futureThread.submit(new HandlerTask(handler,jobInfo));
        //把返回结果加入到回调处理
        CallbackThread.getInstance().pushFuture(new CallBackParam(jobInfo.getId(),future));

        return Result.success(server);//返回地址
    }

}
