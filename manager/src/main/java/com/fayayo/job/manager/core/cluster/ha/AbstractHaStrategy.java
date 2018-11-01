package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.transport.client.NettyClient;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
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
    public ResponsePacket doCall(RequestPacket request, LoadBalance loadBalance) {
        log.info("{}HaStrategy start on loadBalance:{}", Constants.LOG_PREFIX, loadBalance.getClass().getSimpleName());
        return call(request,loadBalance);
    }

    protected abstract ResponsePacket call(RequestPacket request, LoadBalance loadBalance);


    public ResponsePacket request(Endpoint endpoint,RequestPacket request){
        log.info("{}Start request:{}",Constants.LOG_PREFIX,request.toString());
        NettyClient client=new NettyClient(endpoint.getHost(),endpoint.getPort());
        endpoint.incrActiveCount();
        try {
            if(client.open()){
                ResponsePacket response=client.request(request);
                return response;
            }else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //TODO
            throw new RuntimeException("发送RPC请求异常!!!");
        }finally {
            endpoint.decrActiveCount();
            //关闭资源
            client.close();
        }
    }



}
