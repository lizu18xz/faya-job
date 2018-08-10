package com.fayayo.job.manager.core.cluster.loadbalance;

import com.fayayo.job.common.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dalizu on 2018/8/10.
 * @version v1.0
 * @desc 负载均衡算法 轮训
 */
public class RoundRobinLoadBalance implements LoadBalance {

    private volatile List<Endpoint> endpoints;
    @Override
    public void onRefresh(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    private AtomicInteger idx = new AtomicInteger(0);

    @Override
    public Endpoint select() {

        int index = getNextNonNegative();
        for (int i = 0; i < endpoints.size(); i++) {
            Endpoint ref = endpoints.get((i + index) % endpoints.size());
            return ref;
        }
        return null;
    }

    private int getNextNonNegative() {
        return MathUtil.getNonNegative(idx.incrementAndGet());
    }

    public static void main(String[] args) throws InterruptedException {
        LoadBalance loadBalance=new RoundRobinLoadBalance();
        List<Endpoint>list=new ArrayList<>();
        list.add(new Endpoint("10",9001));
        list.add(new Endpoint("11",9002));
        list.add(new Endpoint("12",9003));
        list.add(new Endpoint("13",9004));

        loadBalance.onRefresh(list);//刷新地址
        while (true){
            Endpoint endpoint=loadBalance.select();//获取一个地址
            System.out.println(endpoint.toString());
            Thread.sleep(1000);
        }
    }

}