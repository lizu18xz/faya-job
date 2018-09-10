package com.fayayo.job.core.closable;

/**
 * @author dalizu on 2018/8/16.
 * @version v1.0
 */
public interface Closable<T> {
    void closeResource();
}
