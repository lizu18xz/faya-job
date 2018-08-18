package com.fayayo.job.manager.core.cluster;


import java.util.List;


public interface LoadBalance {

    Endpoint select();

    void onRefresh(List<Endpoint> endpoints);

    void selectToHolder(List<Endpoint> endpointHolder);
}
