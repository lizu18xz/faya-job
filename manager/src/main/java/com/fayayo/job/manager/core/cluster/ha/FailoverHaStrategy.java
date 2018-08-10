package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.manager.core.cluster.loadbalance.LoadBalance;

public class FailoverHaStrategy extends AbstractHaStrategy {

    //TODO  重试策略的使用
    @Override
    public void call(Integer jobId, LoadBalance loadBalance) {

        int tryCount =3;
        // 如果有问题，则设置为不重试
        if (tryCount < 0) {
            tryCount = 0;
        }
        for (int i = 0; i <= tryCount; i++) {
            try {

            } catch (RuntimeException e) {
                // 对于业务异常，直接抛出
                /*if (ExceptionUtil.isBizException(e)) {
                    throw e;
                } else if (i >= tryCount) {
                    throw e;
                }*/
            }
        }
    }
}
