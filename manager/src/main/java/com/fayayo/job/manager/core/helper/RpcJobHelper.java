package com.fayayo.job.manager.core.helper;

import com.fayayo.job.core.closable.Closable;
import com.fayayo.job.core.closable.ShutDownHook;
import com.fayayo.job.core.thread.StandardThreadExecutor;
import com.fayayo.job.core.thread.StandardThreadManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/10.
 * @version v1.0
 * @desc 处理新增job的具体逻辑 加入到线程池
 */
@Slf4j
public class RpcJobHelper implements Closable{

    //定义一个线程池用来提交任务
    private StandardThreadExecutor rpcJobThreadPool= StandardThreadManager.rpcJobThreadPool();

    private RpcJobHelper() {
        ShutDownHook.registerShutdownHook(this);
    }

    private static class RpcJobHelperHolder{
        private static final RpcJobHelper INSTANCE=new RpcJobHelper();
    }

    public static final RpcJobHelper getInstance(){

        return RpcJobHelperHolder.INSTANCE;
    }

    /**
     *@描述 提交job信息到此处
     */
    public void add(String jobId){
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

    @Override
    public void closeResource() {
        log.info("close Rpc pool");
        toStop();
    }

}
