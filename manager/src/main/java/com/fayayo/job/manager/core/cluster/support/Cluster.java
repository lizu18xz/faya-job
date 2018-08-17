
package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.manager.core.cluster.ha.HaStrategy;
import com.fayayo.job.manager.core.cluster.loadbalance.LoadBalance;

public interface Cluster{

    void call(Integer jobId);

    void setLoadBalance(LoadBalance loadBalance);

    void setHaStrategy(HaStrategy haStrategy);

    LoadBalance getLoadBalance();
}
