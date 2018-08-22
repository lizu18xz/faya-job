package com.fayayo.job.manager.core.cluster.loadbalance;
import com.fayayo.job.common.util.MathUtil;
import com.fayayo.job.core.transport.bean.DefaultRequest;
import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author dalizu on 2018/8/10.
 * @version v1.0
 * @desc 负载均衡算法 hash  暂时不开启
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    /**
     * 默认的consistent的hash的数量
     */
    public static final int DEFAULT_CONSISTENT_HASH_BASE_LOOP = 1000;

    private List<Endpoint> consistentHashReferers;

    @Override
    public void onRefresh(List<Endpoint> endpoints) {
        super.onRefresh(endpoints);

        List<Endpoint> copyReferers = new ArrayList<Endpoint>(endpoints);
        List<Endpoint> tempRefers = new ArrayList<Endpoint>();
        for (int i = 0; i < DEFAULT_CONSISTENT_HASH_BASE_LOOP; i++) {
            Collections.shuffle(copyReferers);
            for (Endpoint ref : copyReferers) {
                tempRefers.add(ref);
            }
        }
        consistentHashReferers = tempRefers;
    }

    @Override
    protected void doSelectToHolder(Request request, List<Endpoint> refersHolder) {
        List<Endpoint> endpoints = getEndpoints();

        int hash = getHash(request);
        for (int i = 0; i < endpoints.size(); i++) {
            Endpoint ref = consistentHashReferers.get((hash + i) % consistentHashReferers.size());
                refersHolder.add(ref);
        }
    }

    @Override
    public Endpoint doSelect(Request request) {
        int hash = getHash(request);
        Endpoint ref;
        for (int i = 0; i < getEndpoints().size(); i++) {
            ref = consistentHashReferers.get((hash + i) % consistentHashReferers.size());
            return ref;
        }
        return null;
    }

    private int getHash(Request request) {
        int hashcode;
        if (request.getArguments() == null || request.getArguments().length == 0) {
            hashcode = request.hashCode();
        } else {
            hashcode = Arrays.hashCode(request.getArguments());
        }
        return MathUtil.getNonNegative(hashcode);
    }


    public static void main(String[] args) throws InterruptedException {
        LoadBalance loadBalance=new ConsistentHashLoadBalance();
        List<Endpoint>list=new ArrayList<>();
        list.add(new Endpoint("10",9001));
        list.add(new Endpoint("11",9002));
        list.add(new Endpoint("12",9003));
        list.add(new Endpoint("13",9004));
        loadBalance.onRefresh(list);//刷新地址
        while (true){
            Endpoint endpoint=loadBalance.select(new DefaultRequest());//获取一个地址
            System.out.println(endpoint.toString());
            Thread.sleep(1000);
        }
    }

}
