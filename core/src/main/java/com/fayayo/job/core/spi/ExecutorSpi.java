package com.fayayo.job.core.spi;

import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.transport.spi.Response;

/**
 * @author dalizu on 2018/8/18.
 * @version v1.0
 * @desc 执行具体方法的接口
 */
public interface ExecutorSpi {


    Response run(JobInfoParam jobInfo);



}
