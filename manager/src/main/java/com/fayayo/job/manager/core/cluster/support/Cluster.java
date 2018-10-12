
package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import com.fayayo.job.manager.core.cluster.HaStrategy;
import com.fayayo.job.manager.core.cluster.LoadBalance;

public interface Cluster {

    ResponsePacket call(RequestPacket request);

    void setLoadBalance(LoadBalance loadBalance);

    void setHaStrategy(HaStrategy haStrategy);

    void setRetries(Integer retries);

    Integer getRetries();

    LoadBalance getLoadBalance();
}
