package com.fayayo.job.core.service;

import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.executor.result.LogResult;
import com.fayayo.job.core.executor.result.Result;
import org.apache.hadoop.ipc.VersionedProtocol;

/**
 * @author dalizu on 2018/8/18.
 * @version v1.0
 * @desc 执行具体方法的接口
 */
public interface ExecutorRun extends VersionedProtocol {

    //定义一个版本号
    public static long versionID = 1;

    Result<?> run(JobInfoParam jobInfo);


    Result<LogResult> log(String executorType, String logId, long pointer);


}
