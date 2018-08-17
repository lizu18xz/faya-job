package com.fayayo.job.manager.core.proxy;


/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc ProxyFactory
 */
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz);

}
