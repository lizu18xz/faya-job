package com.fayayo.job.manager.core.cluster.ha;


import com.fayayo.job.core.spi.ExecutorSpi;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import com.fayayo.job.manager.core.proxy.ProxyFactory;
import com.fayayo.job.manager.core.proxy.spi.JdkProxyFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc 快速失败，失败之后直接报错
 */
@Slf4j
public class FailfastHaStrategy implements HaStrategy {

    //快速失败策略
    @Override
    public void call(Integer jobId,LoadBalance loadBalance) {
        //获取执行的服务
        Endpoint endpoint=loadBalance.select();
        log.info("FailfastHaStrategy start to call ......{}",endpoint.getHost());
        //定义一个接口，获取其代理类，然后调用   走invock方法，发送请求，  服务端获取方法名称然后调用
        ExecutorSpi executorSpi=getExecutorSpi(endpoint);
        executorSpi.run();//执行具体的方法 invoke
    }

     /**
       *@描述 获取代理类
     */
    private ExecutorSpi getExecutorSpi(Endpoint endpoint) {
        ProxyFactory proxyFactory=new JdkProxyFactory();
        return proxyFactory.getProxy(ExecutorSpi.class, endpoint);
    }

}
