package com.fayayo.job.core.transport.thread;

import com.fayayo.job.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc 自定义线程池
 */
@Slf4j
public class TransportThreadPool {

    private ThreadPoolExecutor threadPoolExecutor;
    private int coreThread = 10;
    private int maxThread = 50;
    private int maxTimeRecycle = 60;

    public TransportThreadPool() {
        log.info("{}初始化TransportThreadPool......", Constants.LOG_PREFIX);
        //无界 也可以指定大小的队列
        final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(maxThread);
        threadPoolExecutor = new ThreadPoolExecutor(coreThread, maxThread, maxTimeRecycle, TimeUnit.SECONDS,
                queue);
        //自定义策略
        threadPoolExecutor.setRejectedExecutionHandler(new BlockRejectedExecutionHandler());
        //监控线程
        Thread daemonThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (queue.size() > maxThread) {
                        log.info("{}current thread task size:{} exceed thread pool define size:{}",Constants.LOG_PREFIX,queue.size(),maxThread);
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

    /**
     *@描述 拒绝策略
     */
    private class BlockRejectedExecutionHandler implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
            try {
                log.info("current thread task exceed thread pool define size : {}", maxThread);
                // 核心改造点，由blockingqueue的offer改成put阻塞方法
                executor.getQueue().put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }

    public Future<?> submit(Callable<?> command) {
        return threadPoolExecutor.submit(command);
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    public int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }

}
