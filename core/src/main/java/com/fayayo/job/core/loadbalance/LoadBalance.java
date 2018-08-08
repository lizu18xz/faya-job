package com.fayayo.job.core.loadbalance;

import java.util.List;


public interface LoadBalance {

    Endpoint select();

    void onRefresh(List<Endpoint> endpoints);

}
