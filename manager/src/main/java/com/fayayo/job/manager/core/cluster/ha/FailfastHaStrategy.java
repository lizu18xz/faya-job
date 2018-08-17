package com.fayayo.job.manager.core.cluster.ha;


import com.fayayo.job.manager.core.cluster.loadbalance.LoadBalance;
/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc 快速失败，失败之后直接报错
 */
public class FailfastHaStrategy extends AbstractHaStrategy {

    //TODO 具体失败策略执行
    @Override
    public void call(Integer jobId,LoadBalance loadBalance) {
        loadBalance.select();

    }

}
