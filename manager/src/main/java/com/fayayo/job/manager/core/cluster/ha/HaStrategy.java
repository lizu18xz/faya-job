package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.manager.core.cluster.loadbalance.LoadBalance;

public interface HaStrategy {

    void call(Integer jobId, LoadBalance loadBalance);

}
