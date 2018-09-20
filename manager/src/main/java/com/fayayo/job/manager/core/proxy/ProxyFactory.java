package com.fayayo.job.manager.core.proxy;


import com.fayayo.job.core.extension.Scope;
import com.fayayo.job.core.extension.Spi;
import com.fayayo.job.manager.core.cluster.support.Cluster;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc ProxyFactory
 */
@Spi(scope = Scope.PROTOTYPE)
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz, Cluster cluster);

}
