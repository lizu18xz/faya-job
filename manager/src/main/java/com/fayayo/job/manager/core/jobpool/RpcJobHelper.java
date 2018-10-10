package com.fayayo.job.manager.core.jobpool;

import com.fayayo.job.core.pool.StandardThreadExecutor;
import com.fayayo.job.core.pool.StandardThreadManager;
import com.fayayo.job.manager.core.trigger.TriggerHelper;

/**
 * @author dalizu on 2018/8/10.
 * @version v1.0
 * @desc 处理新增job的具体逻辑 加入到线程池
 */
public class RpcJobHelper {

    //定义一个线程池用来提交任务
    private StandardThreadExecutor rpcJobThreadPool= StandardThreadManager.rpcJobThreadPool();

    private RpcJobHelper() {

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                RpcJobHelper.getInstance().toStop();//关闭资源
            }
        });

    }

    private static class RpcJobHelperHolder{
        private static final RpcJobHelper instance=new RpcJobHelper();
    }

    public static final RpcJobHelper getInstance(){

        return RpcJobHelperHolder.instance;
    }

    /**
     *@描述 提交job信息到此处
     */
    public void addJobInPool(String jobId){

        rpcJobThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                TriggerHelper.Trigger(jobId);
            }
        });

    }


     /**
       *@描述 关闭线程池
     */
    public void toStop(){
        rpcJobThreadPool.shutdown();
    }


}
