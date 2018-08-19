package com.fayayo.job.core.spi;

import com.fayayo.job.core.bean.Response;
import com.fayayo.job.entity.JobInfo;

/**
 * @author dalizu on 2018/8/18.
 * @version v1.0
 * @desc 执行具体方法的接口
 */
public interface ExecutorSpi {


    Response run(JobInfo jobInfo);



}
