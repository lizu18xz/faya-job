package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.transport.NettyClient;
import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/19.
 * @version v1.0
 * @desc
 */
@Slf4j
public abstract class AbstractHaStrategy implements HaStrategy {

    @Override
    public Response doCall(Request request, LoadBalance loadBalance) {
        log.info("{}HaStrategy start on loadBalance:{}", Constants.LOG_PREFIX, loadBalance);
        return call(request,loadBalance);
    }

    protected abstract Response call(Request request, LoadBalance loadBalance);


    public Response request(Endpoint endpoint,Request request){
        log.info("{}start request......:{}",Constants.LOG_PREFIX,request.toString());

        NettyClient client=new NettyClient(endpoint.getHost(),endpoint.getPort());
        try {
            client.open();
            Response response=client.request(request);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
            return null;
        }
    }

}
