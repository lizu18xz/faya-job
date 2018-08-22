
package com.fayayo.job.manager.core.cluster.loadbalance;

import com.fayayo.job.core.transport.bean.DefaultRequest;
import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author dalizu on 2018/8/10.
 * @version v1.0
 * @desc 负载均衡算法 随机
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    public Endpoint doSelect(Request request) {

        List<Endpoint> endpoints = getEndpoints();

        int idx = (int) (ThreadLocalRandom.current().nextDouble() * endpoints.size());
        for (int i = 0; i < endpoints.size(); i++) {
            Endpoint ref = endpoints.get((i + idx) % endpoints.size());
            //TODO 判断是否存活
            return ref;
        }
        return null;
    }

    @Override
    protected void doSelectToHolder(Request request,List<Endpoint> refersHolder) {
        List<Endpoint> endpoints = getEndpoints();
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * endpoints.size());
        for (int i = 0; i < endpoints.size(); i++) {
            Endpoint endpoint = endpoints.get((i + idx) % endpoints.size());
            refersHolder.add(endpoint);
        }
    }

    public static void main(String[] args) {

        LoadBalance loadBalance = new RandomLoadBalance();
        List<Endpoint> list = new ArrayList<>();
        list.add(new Endpoint("10", 9001));
        list.add(new Endpoint("11", 9002));
        list.add(new Endpoint("12", 9003));
        list.add(new Endpoint("13", 9004));

        loadBalance.onRefresh(list);//刷新地址
        Endpoint endpoint = loadBalance.select(new DefaultRequest());//获取一个地址
        System.out.println(endpoint.toString());
    }

}
