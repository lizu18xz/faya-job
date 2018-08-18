package com.fayayo.job.manager.core.cluster.ha;


import com.fayayo.job.manager.core.cluster.LoadBalance;

public interface HaStrategy {

    void call(Integer jobId, LoadBalance loadBalance);

}
