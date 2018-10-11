

package com.fayayo.job.core.transport.future;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 异步任务接口
 */
public interface Future {
    /**
     * cancle the task
     * 
     * @return
     */
    boolean cancel();

    /**
     * task cancelled
     * 
     * @return
     */
    boolean isCancelled();

    /**
     * task is complete : normal or exception
     * 
     * @return
     */
    boolean isDone();

    /**
     * isDone() & normal
     * 
     * @return
     */
    boolean isSuccess();

    /**
     * if task is success, return the result.
     * 
     * @throws Exception when timeout, cancel, onFailure
     * @return
     */
    Object getValue();

    /**
     * if task is done or cancle, return the exception
     * 
     * @return
     */
    Exception getException();

    /**
     * add future listener , when task is success，failure, timeout, cancel, it will be called
     * 
     * @param listener
     */
    void addListener(FutureListener listener);

}
