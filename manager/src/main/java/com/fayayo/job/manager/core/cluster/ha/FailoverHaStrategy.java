package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.extension.SpiMeta;
import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc 扩展类 通过负载均衡器选择一组Referer（选择算法根据不同LB有不同实现），
 * 然后只调用第一个，当出现错误的时候（非业务异常），我们尝试调用n次（可配置），
 * 如果n次都失败了，那么我们就调用下一个Referer，如果这组Referer都调用失败，则抛出异常。
 * 如果扩展类有SpiMeta的注解，那么获取对应的name，如果没有的话获取classname
 */
@SpiMeta(name = "failover")
@Slf4j
public class FailoverHaStrategy extends AbstractHaStrategy {

    protected ThreadLocal<List<Endpoint>> endpointHolder = new ThreadLocal<List<Endpoint>>() {
        @Override
        protected java.util.List<Endpoint> initialValue() {
            return new ArrayList<Endpoint>();
        }
    };

    public Response call(Request request, LoadBalance loadBalance) {

        //根据规则获取一组endpoint
        List<Endpoint> endpointList = selectReferers(request,loadBalance);
        if (endpointList.isEmpty()) {
            throw new CommonException(999999, String.format("FailoverHaStrategy No Endpoint　loadbalance:%s", loadBalance));
        }
        int tryCount = request.getRetries();//获取用户配置的重试次数
        // 如果有问题，则设置为不重试
        if (tryCount < 0) {
            tryCount = 0;
        }
        for (int i = 0; i <= tryCount; i++) {
            Endpoint endpoint = endpointList.get(i % endpointList.size());
            log.info("{}FailoverHaStrategy start to call ......{},tryCount:{},request:{}", Constants.LOG_PREFIX,endpoint.getHost(),(i+1),request.toString());
            try {
                //获取nettyClient  发送RPC请求
                return request(endpoint,request);

            } catch (RuntimeException e) {
                // 对于业务异常，直接抛出，不进行重试
                if (e instanceof CommonException) {
                    throw e;
                } else if (i >= tryCount) {
                    log.info("{}tryCount is over......throw e",Constants.LOG_PREFIX);
                    throw e;
                }
                log.info("{}try run ,tryCount:{}",Constants.LOG_PREFIX,(i+1));
            }
        }
        throw new CommonException(999999,"FailoverHaStrategy.call should not come here!");
    }

    protected List<Endpoint> selectReferers(Request request,LoadBalance loadBalance) {
        List<Endpoint> endpoints = endpointHolder.get();
        endpoints.clear();
        loadBalance.selectToHolder(request,endpoints);
        return endpoints;
    }

}
