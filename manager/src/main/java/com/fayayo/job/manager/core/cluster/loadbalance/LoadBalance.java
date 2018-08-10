package com.fayayo.job.manager.core.cluster.loadbalance;

import java.util.List;


public interface LoadBalance {

    Endpoint select();

    void onRefresh(List<Endpoint> endpoints);

}
