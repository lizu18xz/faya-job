package com.fayayo.job.core.loadbalance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WeightRoundRobinLoadBalance implements LoadBalance {


    private volatile EndpointHolder endpointHolder;

    public WeightRoundRobinLoadBalance() {
    }

    class EndpointHolder {

        private int randomKeySize;
        private List<Endpoint> randomKeyList = new ArrayList<>();
        AtomicInteger cursor = new AtomicInteger(0);

        EndpointHolder(List<Endpoint> endpoints) {
            System.out.println("WeightRoundRobinLoadBalance build new EndpointHolder. weights:" + endpoints);
            List<Integer> weightsArr = endpoints.stream().map(Endpoint::getWeight).collect(Collectors.toList());
            // 求出最大公约数，若不为1，对权重做除法
            int weightGcd = findGcd(weightsArr.toArray(new Integer[]{}));

            if (weightGcd != 1) {
                for (Endpoint endpoint : endpoints) {
                    endpoint.setWeight(endpoint.getWeight() / weightGcd);
                }
            }
            for (Endpoint endpoint : endpoints) {
                for (int i = 0; i < endpoint.getWeight(); i++) {
                    randomKeyList.add(endpoint);
                }
            }
            Collections.shuffle(randomKeyList);
            randomKeySize = randomKeyList.size();
        }

        Endpoint next() {
            Endpoint endpoint = randomKeyList.get(Math.abs(cursor.getAndAdd(1)) % randomKeySize);
            return endpoint;
        }

        // 求最大公约数
        private int findGcd(int n, int m) {
            return (n == 0 || m == 0) ? n + m : findGcd(m, n % m);
        }

        // 求最大公约数
        private int findGcd(Integer[] arr) {
            if (arr.length == 1) return arr[0];
            int i = 0;
            for (; i < arr.length - 1; i++) {
                arr[i + 1] = findGcd(arr[i], arr[i + 1]);
            }
            return findGcd(arr[i], arr[i - 1]);
        }
    }

    @Override
    public Endpoint select() {
        return endpointHolder.next();
    }

    @Override
    public void onRefresh(List<Endpoint> endpoints) {
        this.endpointHolder = new EndpointHolder(endpoints);
    }

    public Endpoint[] getOriginEndpoints() {
        return this.endpointHolder.randomKeyList.toArray(new Endpoint[]{});
    }


    public static void main(String[] args) {

        LoadBalance loadBalance=new WeightRoundRobinLoadBalance();

        List<Endpoint>list=new ArrayList<>();

        list.add(new Endpoint("10",9001,8));
        list.add(new Endpoint("11",9002,5));
        list.add(new Endpoint("12",9003,7));
        list.add(new Endpoint("13",9004,1));

        loadBalance.onRefresh(list);//刷新地址
        Endpoint endpoint=loadBalance.select();//获取一个地址
        System.out.println(endpoint.toString());


    }



}
