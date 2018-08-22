package com.fayayo.job.manager.core.route;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.transport.bean.DefaultRequest;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;
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
            //解析address:port:weight
            String[]servers=e.split(":");
            if(servers.length==3){
                return new Endpoint(servers[0],Integer.parseInt(servers[1]),Integer.parseInt(servers[2]));//构造Endpoint
            }else if(servers.length==2){
                return new Endpoint(servers[0],Integer.parseInt(servers[1]));//构造Endpoint
            }else {
                log.info("{}服务地址注册格式有误!!!", Constants.LOG_PREFIX);
                return null;
            }
        }).collect(Collectors.toList());

    }

    /**
     *@描述 获取loadBalance
     *@创建人  dalizu
     *@创建时间  2018/8/12
     */
    public LoadBalance getLoadBalance(JobInfoParam jobInfo){
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
                    JobInfoParam jobInfo=new JobInfoParam();
                    jobInfo.setJobLoadBalance(3);
                    jobInfo.setId(1);
                    LoadBalance loadBalance=jobRouteExchange.getLoadBalance(jobInfo);
                    System.out.println(loadBalance.select(new DefaultRequest()).toString());
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

                    JobInfoParam jobInfo1=new JobInfoParam();
                    jobInfo1.setJobLoadBalance(3);
                    jobInfo1.setId(2);
                    LoadBalance loadBalance1=jobRouteExchange1.getLoadBalance(jobInfo1);
                    System.out.println(loadBalance1.select(new DefaultRequest()).toString());
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
