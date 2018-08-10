package com.fayayo.job.manager.core.cluster.ha;


import com.fayayo.job.manager.core.cluster.loadbalance.LoadBalance;

public class FailfastHaStrategy extends AbstractHaStrategy {

    //TODO 具体失败策略执行
    @Override
    public void call(Integer jobId,LoadBalance loadBalance) {
        loadBalance.select();

    }

}
