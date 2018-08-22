package com.fayayo.job.manager.core.cluster;



import com.fayayo.job.core.transport.spi.Request;

import java.util.List;


public interface LoadBalance {

    Endpoint select(Request request);

    void onRefresh(List<Endpoint> endpoints);

    void selectToHolder(Request request,List<Endpoint> endpointHolder);
}
