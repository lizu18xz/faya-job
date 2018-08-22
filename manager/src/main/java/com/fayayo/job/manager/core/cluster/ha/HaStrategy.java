package com.fayayo.job.manager.core.cluster.ha;


import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
import com.fayayo.job.manager.core.cluster.LoadBalance;

public interface HaStrategy {

    Response doCall(Request request, LoadBalance loadBalance);

}
