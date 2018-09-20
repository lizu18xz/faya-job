package com.fayayo.job.manager.core.proxy.impl;

import com.fayayo.job.core.extension.SpiMeta;
import com.fayayo.job.manager.core.cluster.support.Cluster;
import com.fayayo.job.manager.core.proxy.ProxyFactory;
import com.fayayo.job.manager.core.proxy.RefererInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc jdkProxy
 */
@SpiMeta(name = "jdk")
public class JdkProxyFactory implements ProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clz, Cluster cluster) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new RefererInvocationHandler<>(clz,cluster));
    }
}
