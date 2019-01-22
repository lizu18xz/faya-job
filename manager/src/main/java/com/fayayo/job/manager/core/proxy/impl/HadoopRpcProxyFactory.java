package com.fayayo.job.manager.core.proxy.impl;

import com.fayayo.job.core.extension.SpiMeta;
import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.util.RequestIdGenerator;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import com.fayayo.job.manager.core.cluster.support.Cluster;
import com.fayayo.job.manager.core.proxy.ProxyFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author dalizu on 2019/1/22.
 * @version v1.0
 * @desc 使用hadoop-common 下面的RPC,不用自己实现
 */
@SpiMeta(name = "hadoop-rpc")
public class HadoopRpcProxyFactory  implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> clz, Cluster cluster) {

        LoadBalance loadBalance=cluster.getLoadBalance();
        RequestPacket request=new RequestPacket();
        request.setRequestId(RequestIdGenerator.getRequestId());
        Endpoint endpoint=loadBalance.select(request);
        //得到服务器端的一个代理对象
        try {
            return (T) RPC.getProxy(clz,ExecutorRun.versionID ,new InetSocketAddress(endpoint.getHost(), endpoint.getPort()),
                    new Configuration());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
