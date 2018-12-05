package com.fayayo.job.core.callback;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.executor.result.Result;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author dalizu on 2018/9/5.
 * @version v1.0
 * @desc 回调函数线程执行  单列
 */
@Slf4j
public class CallbackThread extends Thread {

    private volatile boolean toStop = false;//停止标志

    private LinkedBlockingQueue<CallBackParam> callBackQueue = new LinkedBlockingQueue<CallBackParam>(128);

    private CallBackHandler callBackHandler;

    public void setCallBackHandler(CallBackHandler callBackHandler) {
        this.callBackHandler = callBackHandler;
    }

    private CallbackThread() {
    }

    private static final CallbackThread instance = new CallbackThread();

    public static CallbackThread getInstance() {
        return instance;
    }

    /**
     * @描述 新增获取任务结果的任务
     */
    public void pushFuture(CallBackParam callBackParam) {
        try {
            log.info("{}Add Future to Queue", Constants.LOG_PREFIX);
            this.callBackQueue.put(callBackParam);//阻塞等待有空闲的位置
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        log.info("{}Wait deal with the run result......", Constants.LOG_PREFIX);
        try {
            while (!toStop) {
                CallBackParam callBackParam = this.callBackQueue.take();//阻塞 doPull
                Result<?> result = (Result<?>) callBackParam.getFuture().get();
                this.callBackHandler.onNewMessageArrived(callBackParam.getJobId(), result);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void toStop() {
        toStop = true;
        CallbackThread.getInstance().interrupt();
    }

    public interface CallBackHandler {
        //消息通知出去
        void onNewMessageArrived(String jobId, Result<?> result);
    }

}
