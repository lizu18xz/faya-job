package com.fayayo.job.core.thread;

import com.fayayo.job.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dalizu on 2018/10/10.
 * @version v1.0
 * @desc  项目中使用线程池时候的统一管理
 */
@Slf4j
public class StandardThreadExecutor extends ThreadPoolExecutor {

    //默认值
    public static final int DEFAULT_MIN_THREADS = 20;
    public static final int DEFAULT_MAX_THREADS = 200;
    public static final int DEFAULT_MAX_IDLE_TIME = 60 * 1000; // 1 minutes

    protected AtomicInteger submittedTasksCount;	// 正在处理的任务数

    public StandardThreadExecutor() {
        this(DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS);
    }

    public StandardThreadExecutor(int coreThread, int maxThreads) {
        this(coreThread, maxThreads, maxThreads);
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, int queueCapacity) {
        this(coreThreads, maxThreads, queueCapacity, Executors.defaultThreadFactory());
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, int queueCapacity, ThreadFactory threadFactory) {
        this(coreThreads, maxThreads, DEFAULT_MAX_IDLE_TIME, TimeUnit.MILLISECONDS, queueCapacity, threadFactory);
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit,
                                  int queueCapacity, ThreadFactory threadFactory) {
        this(coreThreads, maxThreads, keepAliveTime, unit, queueCapacity, threadFactory, new AbortPolicy());
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit,
                                  int queueCapacity, ThreadFactory threadFactory, RejectedExecutionHandler handler) {

        super(coreThreads, maxThreads, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(queueCapacity), threadFactory, handler);
        //log.info("{}初始化一个线程池:{}",Constants.LOG_PREFIX,"StandardThreadExecutor");
        submittedTasksCount = new AtomicInteger(0);

        // 无界队列 可以进行 最大并发任务限制： 队列buffer数 + 最大线程数

        //监控线程
        Thread daemonThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (getQueue().size() > maxThreads) {
                        log.info("{}current callback task size:{} exceed callback thread define size:{}", Constants.LOG_PREFIX,
                                getQueue().size(),maxThreads);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
    }


    public void execute(Runnable command) {
        int count = submittedTasksCount.incrementAndGet();
        // 超过最大的并发任务限制，进行 reject
        // 如果依赖的队列没有长度限制，因此这里进行控制

        try {
            super.execute(command);
        } catch (RejectedExecutionException rx) {
           //异常处理
            getRejectedExecutionHandler().rejectedExecution(command, this);
        }
    }


    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(task);
    }

    public int getSubmittedTasksCount() {
        return this.submittedTasksCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTasksCount.decrementAndGet();
    }

    public void shutdown() {
        log.info("{}关闭线程池",Constants.LOG_PREFIX);
        super.shutdown();
    }

    public int getActiveCount() {
        return super.getActiveCount();
    }


    /**
     *@描述 拒绝策略(阻塞的形式)
     */
    public static class BlockRejectedExecutionHandler implements RejectedExecutionHandler {


        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
            try {
                log.info("current callback task exceed callback thread define size : {}", StandardThreadExecutor.DEFAULT_MAX_THREADS);
                // 核心改造点，由blockingqueue的offer改成put阻塞方法
                executor.getQueue().put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        StandardThreadExecutor mySelfThreadPool=new StandardThreadExecutor(10, 50, 60,
                TimeUnit.SECONDS,50,
                new DefaultThreadFactory("test"),new BlockRejectedExecutionHandler()

        );
        for ( int i=0;i<1000;i++){
            int finalI = i;
            mySelfThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(finalI + " is running."+Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        mySelfThreadPool.shutdown();
    }

}
