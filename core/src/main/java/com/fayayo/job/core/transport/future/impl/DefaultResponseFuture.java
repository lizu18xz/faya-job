package com.fayayo.job.core.transport.future.impl;

import com.fayayo.job.core.transport.exception.NettyServiceException;
import com.fayayo.job.core.transport.future.FutureListener;
import com.fayayo.job.core.transport.future.ResponseFuture;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class DefaultResponseFuture implements ResponseFuture {

    protected volatile FutureState state = FutureState.DOING;

    protected Object lock = new Object();//对象锁

    protected Object result = null;//真正的返回结果

    protected Exception exception = null;//异常返回

    protected long createTime = System.currentTimeMillis();
    protected int timeout = 0;//超时时间
    protected long processTime = 0;

    protected RequestPacket request;
    protected List<FutureListener> listeners;

    public DefaultResponseFuture(RequestPacket requestObj, int timeout) {
        this.request = requestObj;
        this.timeout = timeout;
    }


    /**
       *@描述 当服务端返回信息后，会到客户端的handler中,如果正常返回，会把返回结果传递给此处onSuccess方法进行处理
       *      会唤醒getValue时阻塞的线程
     */
    public void onSuccess(ResponsePacket response) {
        this.result = response.getValue();
        this.processTime = response.getProcessTime();
        done();
    }

    public void onFailure(ResponsePacket response) {
        this.exception = response.getException();
        this.processTime = response.getProcessTime();
        done();
    }

    @Override
    public Object getValue() {
        synchronized (lock) {
            if (!isDoing()) {
                return getValueOrThrowable();
            }
            //没有设置超时时间,会一直阻塞,直到唤醒
            if (timeout <= 0) {
                try {
                    lock.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    cancel(new NettyServiceException(this.getClass().getName() + " getValue InterruptedException : "
                            + request + " cost=" + (System.currentTimeMillis() - createTime)));
                }
                return getValueOrThrowable();//如果不是在运行就报错
            } else {
                long waitTime = timeout - (System.currentTimeMillis() - createTime);

                if (waitTime > 0) {
                    for (; ; ) {
                        try {
                            lock.wait(waitTime);
                        } catch (InterruptedException e) {
                        }

                        if (!isDoing()) {
                            break;
                        } else {
                            waitTime = timeout - (System.currentTimeMillis() - createTime);
                            if (waitTime <= 0) {
                                break;
                            }
                        }
                    }
                }
               //超过设置的超时时间不返回就报错
                if (isDoing()) {
                    timeoutSoCancel();
                }
            }
            //返回
            return getValueOrThrowable();
        }
    }


    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public boolean cancel() {
        Exception e =
                new NettyServiceException(this.getClass().getName() + " task cancel:" + request.toString() + " cost=" + (System.currentTimeMillis() - createTime));
        return cancel(e);
    }

    protected boolean cancel(Exception e) {
        synchronized (lock) {
            if (!isDoing()) {
                return false;
            }
            state = FutureState.CANCELLED;
            exception = e;
            lock.notifyAll();
        }
        notifyListeners();
        return true;
    }

    @Override
    public boolean isCancelled() {
        return state.isCancelledState();
    }

    @Override
    public boolean isDone() {
        return state.isDoneState();
    }

    @Override
    public boolean isSuccess() {
        return isDone() && (exception == null);
    }

    @Override
    public void addListener(FutureListener listener) {
        if (listener == null) {
            throw new NullPointerException("FutureListener is null");
        }

        boolean notifyNow = false;
        synchronized (lock) {
            if (!isDoing()) {
                notifyNow = true;
            } else {
                if (listeners == null) {
                    listeners = new ArrayList<FutureListener>(1);
                }

                listeners.add(listener);
            }
        }

        if (notifyNow) {
            notifyListener(listener);
        }
    }

    private void timeoutSoCancel() {
        this.processTime = System.currentTimeMillis() - createTime;

        synchronized (lock) {
            if (!isDoing()) {
                return;
            }

            state = FutureState.CANCELLED;
            exception =
                    new NettyServiceException(this.getClass().getName() + " request timeout: serverPort="  + request + " cost=" + (System.currentTimeMillis() - createTime));
            lock.notifyAll();
        }

        notifyListeners();
    }

    private void notifyListeners() {
        if (listeners != null) {
            for (FutureListener listener : listeners) {
                notifyListener(listener);
            }
        }
    }

    private void notifyListener(FutureListener listener) {
        try {
            listener.operationComplete(this);
        } catch (Throwable t) {
           log.error(this.getClass().getName() + " notifyListener Error: " + listener.getClass().getSimpleName(), t);
        }
    }

    private boolean isDoing() {
        return state.isDoingState();
    }

    protected boolean done() {
        synchronized (lock) {
            if (!isDoing()) {//默认doing   如果不是这个状态就返回
                return false;
            }
            state = FutureState.DONE;//运行完毕
            lock.notifyAll();
        }
        notifyListeners();//运行完毕
        return true;
    }

    private Object getValueOrThrowable() {
        if (exception != null) {
            throw (exception instanceof RuntimeException) ? (RuntimeException) exception : new NettyServiceException(
                    exception.getMessage());
        }

        return result;
    }


    public long getRequestId() {
        return this.request.getRequestId();
    }


    public int getTimeout() {
        return timeout;
    }

    public long getCreateTime() {
        return createTime;
    }

    public Object getRequestObj() {
        return request;
    }

    public FutureState getState() {
        return state;
    }


}
