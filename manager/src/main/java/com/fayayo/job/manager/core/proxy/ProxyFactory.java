package com.fayayo.job.manager.core.proxy;


import com.fayayo.job.manager.core.cluster.Endpoint;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc ProxyFactory
 */
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz, Endpoint endpoint);

}
