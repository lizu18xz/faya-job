package com.fayayo.job.manager.core.proxy.spi;

import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.proxy.ProxyFactory;
import com.fayayo.job.manager.core.proxy.RefererInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc jdkProxy
 */
public class JdkProxyFactory implements ProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clz, Endpoint endpoint) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new RefererInvocationHandler<>(clz,endpoint));
    }
}
