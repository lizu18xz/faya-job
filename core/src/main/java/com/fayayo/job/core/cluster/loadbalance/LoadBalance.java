package com.fayayo.job.core.cluster.loadbalance;

import java.util.List;


public interface LoadBalance {

    Endpoint select();

    void onRefresh(List<Endpoint> endpoints);

}
