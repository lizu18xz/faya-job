
package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import com.fayayo.job.manager.core.cluster.ha.HaStrategy;

public interface Cluster {

    Response call(Request request);

    void setLoadBalance(LoadBalance loadBalance);

    void setHaStrategy(HaStrategy haStrategy);

    LoadBalance getLoadBalance();
}
