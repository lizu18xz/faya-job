
package com.fayayo.job.core.thread;


import com.fayayo.job.common.constants.Constants;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 自定义的线程工厂，用来产生线程
 *
 */
public class DefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup threadGroup;
    private final AtomicInteger currentThreadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private int priority = Thread.NORM_PRIORITY;
    private boolean isDaemon = false;

    public DefaultThreadFactory() {
        this(Constants.FRAMEWORK_NAME);
    }

    public DefaultThreadFactory(String prefix) {
        this(prefix, false);
    }

    public DefaultThreadFactory(String prefix, boolean isDaemon) {
        this(prefix, isDaemon, Thread.NORM_PRIORITY);
    }

    public DefaultThreadFactory(String prefix, boolean isDaemon, int priority) {
        SecurityManager s = System.getSecurityManager();
        this.threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-callback-";
        this.isDaemon = isDaemon;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, namePrefix + currentThreadNumber.getAndIncrement(), 0);
        thread.setDaemon(isDaemon);
        thread.setPriority(priority);
        return thread;
    }
}
