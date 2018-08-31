package com.fayayo.job.core.executor.handler;

import com.fayayo.job.core.executor.bean.Result;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc 所有执行器任务都继承此接口
 */
public interface JobExecutorHandler {

        Result<?> run ();

}
