
package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
import com.fayayo.job.manager.core.cluster.HaStrategy;
import com.fayayo.job.manager.core.cluster.LoadBalance;

public class ClusterSpi implements Cluster {

    private HaStrategy haStrategy;

    private LoadBalance loadBalance;

    private String jobLogId;//每个任务唯一的ID

    private Integer retries=0;//策略重试的次数

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

    public String getJobLogId() {
        return jobLogId;
    }

    public void setJobLogId(String jobLogId) {
        this.jobLogId = jobLogId;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }
}
