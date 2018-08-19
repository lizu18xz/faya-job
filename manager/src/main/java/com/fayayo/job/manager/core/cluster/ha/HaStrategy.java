package com.fayayo.job.manager.core.cluster.ha;


import com.fayayo.job.core.bean.Request;
import com.fayayo.job.core.bean.Response;
import com.fayayo.job.manager.core.cluster.LoadBalance;

public interface HaStrategy {

    Response doCall(Request request,LoadBalance loadBalance);

}
