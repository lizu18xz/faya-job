package com.fayayo.job.core.spi.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.spi.ExecutorSpi;
import com.fayayo.job.core.transport.spi.Response;
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
    public Response run(JobInfoParam jobInfo) {
        log.info("{}start to run job......params:{}", Constants.LOG_PREFIX,jobInfo);

        //获取返回
        return null;
    }

}
