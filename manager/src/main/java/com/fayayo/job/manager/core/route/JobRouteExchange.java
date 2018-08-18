package com.fayayo.job.manager.core.route;

import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.manager.core.cluster.loadbalance.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dalizu on 2018/8/12.
 * @version v1.0
 * @desc 路由选择器
 */
@Slf4j
public class JobRouteExchange {


    private List<Endpoint>endpoints;

    public JobRouteExchange(List<String>addressList) {
        endpoints=addressList.stream().map(e->{

            return new Endpoint(e,1,1);//构造Endpoint

        }).collect(Collectors.toList());

    }

    /**
     *@描述 获取loadBalance
     *@创建人  dalizu
     *@创建时间  2018/8/12
     */
    public LoadBalance getLoadBalance(JobInfo jobInfo){
        LoadBalance loadBalance=null;
        loadBalance=JobLoadBalanceFactory.getLoadBalance(jobInfo);
        loadBalance.onRefresh(endpoints);
        return loadBalance;
    }


    public static void main(String[] args) throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    List<String>list=new ArrayList<>();
                    list.add("10.10.10.1");
                    list.add("10.10.10.2");
                    list.add("10.10.10.3");
                    JobRouteExchange jobRouteExchange=new JobRouteExchange(list);

                    JobInfo jobInfo=new JobInfo();
                    jobInfo.setJobLoadBalance(3);
                    jobInfo.setId(1);
                    LoadBalance loadBalance=jobRouteExchange.getLoadBalance(jobInfo);

                    System.out.println(loadBalance.select().toString());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    List<String>lists=new ArrayList<>();
                    lists.add("10.10.10.99");
                    lists.add("10.10.10.98");
                    lists.add("10.10.10.87");
                    JobRouteExchange jobRouteExchange1=new JobRouteExchange(lists);

                    JobInfo jobInfo1=new JobInfo();
                    jobInfo1.setJobLoadBalance(3);
                    jobInfo1.setId(2);
                    LoadBalance loadBalance1=jobRouteExchange1.getLoadBalance(jobInfo1);

                    System.out.println(loadBalance1.select().toString());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }





}
