package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.transport.future.ResponseFuture;
import com.fayayo.job.core.transport.client.NettyClient;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.HaStrategy;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/19.
 * @version v1.0
 * @desc Ha的处理层
 */
@Slf4j
public abstract class AbstractHaStrategy implements HaStrategy {

    @Override
    public ResponseFuture doCall(RequestPacket request, LoadBalance loadBalance) {
        log.info("{}HaStrategy start on loadBalance:{}", Constants.LOG_PREFIX, loadBalance);
        return call(request,loadBalance);
    }

    protected abstract ResponseFuture call(RequestPacket request, LoadBalance loadBalance);


    public ResponseFuture request(Endpoint endpoint,RequestPacket request){
        log.info("{}start request......:{}",Constants.LOG_PREFIX,request.toString());
        NettyClient client=new NettyClient(endpoint.getHost(),endpoint.getPort());
        endpoint.incrActiveCount();
        try {
            client.open();
            ResponseFuture response=client.request(request);//返回的是DefaultResponseFuture
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
            return null;
        }finally {
            endpoint.decrActiveCount();
        }
    }



}
