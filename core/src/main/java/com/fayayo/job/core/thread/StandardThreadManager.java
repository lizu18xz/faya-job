package com.fayayo.job.core.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author dalizu on 2018/10/10.
 * @version v1.0
 * @desc 统一管理创建的线程池
 */
public class StandardThreadManager {

    //每个执行器拥有的线程池(根据机器等信息合理分配)
    public static StandardThreadExecutor futureThread(){

        return new StandardThreadExecutor(10, 30, 60, TimeUnit.SECONDS,30,
                new DefaultThreadFactory("executor"),
                new StandardThreadExecutor.BlockRejectedExecutionHandler());
    }


    //管理端提交任务的线程池
    public static StandardThreadExecutor rpcJobThreadPool(){
        return new StandardThreadExecutor(10, 50, 60, TimeUnit.SECONDS,50,
                new DefaultThreadFactory("rpcJob"),
                new StandardThreadExecutor.BlockRejectedExecutionHandler());
    }



    public static StandardThreadExecutor transportThreadPool(){
        return new StandardThreadExecutor(10, 50, 60, TimeUnit.SECONDS,50,
                new DefaultThreadFactory("transport"),
                new StandardThreadExecutor.BlockRejectedExecutionHandler());
    }



}
