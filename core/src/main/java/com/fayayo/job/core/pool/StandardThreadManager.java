package com.fayayo.job.core.pool;

import java.util.concurrent.TimeUnit;

/**
 * @author dalizu on 2018/10/10.
 * @version v1.0
 * @desc 统一管理创建的线程池
 */
public class StandardThreadManager {

    public static StandardThreadExecutor futureThread(){

        return new StandardThreadExecutor(10, 50, 60, TimeUnit.SECONDS,50,
                new DefaultThreadFactory("ExecutorRunImpl"),
                new StandardThreadExecutor.BlockRejectedExecutionHandler());
    }


    public static StandardThreadExecutor rpcJobThreadPool(){
        return new StandardThreadExecutor(10, 50, 60, TimeUnit.SECONDS,50,
                new DefaultThreadFactory("RpcJobHelper"),
                new StandardThreadExecutor.BlockRejectedExecutionHandler());
    }

}
