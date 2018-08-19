package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.bean.Request;
import com.fayayo.job.core.bean.Response;
import com.fayayo.job.core.spi.ExecutorSpi;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import com.fayayo.job.manager.core.proxy.ProxyFactory;
import com.fayayo.job.manager.core.proxy.spi.JdkProxyFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/19.
 * @version v1.0
 * @desc
 */
@Slf4j
public abstract class AbstractHaStrategy implements HaStrategy {

    @Override
    public Response doCall(Request request,LoadBalance loadBalance) {
        log.info("{}HaStrategy start on loadBalance:{}", Constants.LOG_PREFIX, loadBalance);
        return call(request,loadBalance);
    }

    protected abstract Response call(Request request,LoadBalance loadBalance);


    public void request(){
        log.info("start request......");
        int a=1/0;
    }



}
