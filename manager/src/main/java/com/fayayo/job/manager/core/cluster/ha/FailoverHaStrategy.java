package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.manager.core.cluster.loadbalance.LoadBalance;
/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc 通过负载均衡器选择一组Referer（选择算法根据不同LB有不同实现），
 * 然后只调用第一个，当出现错误的时候（非业务异常），我们尝试调用n次（可配置），
 * 如果n次都失败了，那么我们就调用下一个Referer，如果这组Referer都调用失败，则抛出异常。
 */
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
