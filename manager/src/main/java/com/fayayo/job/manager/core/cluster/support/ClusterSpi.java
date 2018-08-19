
package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.core.bean.Request;
import com.fayayo.job.core.bean.Response;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import com.fayayo.job.manager.core.cluster.ha.HaStrategy;

public class ClusterSpi implements Cluster {

    private HaStrategy haStrategy;

    private LoadBalance loadBalance;

    public ClusterSpi(HaStrategy haStrategy, LoadBalance loadBalance) {
        this.haStrategy = haStrategy;
        this.loadBalance = loadBalance;
    }

    @Override
    public Response call(Request request) {
        try {
            return haStrategy.doCall(request,loadBalance);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO 错误信息处理
            return null;
        }
    }

    @Override
    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public void setHaStrategy(HaStrategy haStrategy) {
        this.haStrategy = haStrategy;
    }

    @Override
    public LoadBalance getLoadBalance() {
        return this.loadBalance;
    }


}
