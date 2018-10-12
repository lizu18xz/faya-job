package com.fayayo.job.manager.core.cluster.ha;


import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.extension.SpiMeta;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc 扩展类 快速失败，失败之后直接报错
 * 如果扩展类有SpiMeta的注解，那么获取对应的name，如果没有的话获取classname
 */
@SpiMeta(name = "failfast")
@Slf4j
public class FailfastHaStrategy extends AbstractHaStrategy {

    //快速失败策略
    public ResponsePacket call(RequestPacket request, LoadBalance loadBalance) {
        //获取执行的服务
        Endpoint endpoint = loadBalance.select(request);
        log.info("{}FailfastHaStrategy start to call ......{},request:{}", Constants.LOG_PREFIX,endpoint.getHost(),request.toString());

        //获取nettyClient  发送RPC请求

        return request(endpoint,request);
    }




}
