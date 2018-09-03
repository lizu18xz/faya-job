package com.fayayo.job.core.spi.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.JobTypeEnums;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.executor.JobExecutor;
import com.fayayo.job.core.executor.bean.Result;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import com.fayayo.job.core.spi.ExecutorSpi;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/18.
 * @version v1.0
 * @desc 具体执行任务的类
 */
@Slf4j
public class ExecutorSpiImpl implements ExecutorSpi{

     /**
       *@描述 真正执行业务逻辑的地方,在rpc server里面通过反射调用
     */
    @Override
    public Result<?> run(JobInfoParam jobInfo) {
        log.info("{}start to run job......params:{}", Constants.LOG_PREFIX,jobInfo.toString());
        String type=jobInfo.getJobType();

        if(type.equalsIgnoreCase(JobTypeEnums.BEAN.getName())){
            log.info("{}执行器类型:{}",Constants.LOG_PREFIX,jobInfo.getExecutorType());
            //获取具体执行的服务
            JobExecutorHandler handler=JobExecutor.getHandler();

            return handler.run();

        }else {
            log.info("{}暂未开放其他类型处理器!!!!!!",Constants.LOG_PREFIX);
        }
        return null;
    }

}
