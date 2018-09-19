
package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
import com.fayayo.job.manager.core.cluster.HaStrategy;
import com.fayayo.job.manager.core.cluster.LoadBalance;

public interface Cluster {

    Response call(Request request);

    void setLoadBalance(LoadBalance loadBalance);

    void setHaStrategy(HaStrategy haStrategy);

    void setRetries(Integer retries);

    Integer getRetries();

    LoadBalance getLoadBalance();
}
