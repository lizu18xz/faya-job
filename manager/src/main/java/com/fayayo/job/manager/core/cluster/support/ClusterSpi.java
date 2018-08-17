
package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.manager.core.cluster.ha.HaStrategy;
import com.fayayo.job.manager.core.cluster.loadbalance.LoadBalance;

public class ClusterSpi implements Cluster {

    private HaStrategy haStrategy;

    private LoadBalance loadBalance;

    @Override
    public void call(Integer jobId) {
            try {
                haStrategy.call(jobId, loadBalance);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void setLoadBalance(LoadBalance loadBalance) {
            this.loadBalance=loadBalance;
    }

    @Override
    public void setHaStrategy(HaStrategy haStrategy) {
            this.haStrategy=haStrategy;
    }

    @Override
    public LoadBalance getLoadBalance() {
        return this.loadBalance;
    }


}
