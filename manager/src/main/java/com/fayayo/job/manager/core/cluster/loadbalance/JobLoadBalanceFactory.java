package com.fayayo.job.manager.core.cluster.loadbalance;

import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.manager.core.cluster.LoadBalance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author dalizu on 2018/8/12.
 * @version v1.0
 * @desc 工厂类
 */
public class JobLoadBalanceFactory {


    /**
     *@描述  存储策略  每个任务对应自己的策略
     */
    private static final Map<Integer,LoadBalance> jobMap=new ConcurrentHashMap<Integer,LoadBalance>();


    public static LoadBalance getLoadBalance(JobInfo jobInfo){

        LoadBalance loadBalance=null;

        Integer jobId=jobInfo.getId();//获取jobId
        //判断是否已经存在
        loadBalance=jobMap.get(jobId);
        if(loadBalance!=null){
            return loadBalance;
        }
        //获取类型
        Integer loadbalanceType=jobInfo.getJobLoadBalance();

        if(loadbalanceType==JobLoadBalanceEnums.HASH.getCode()){

            loadBalance=new ConsistentHashLoadBalance();

        }else if(loadbalanceType==JobLoadBalanceEnums.RANDOM.getCode()){

            loadBalance= new RandomLoadBalance();

        }else if(loadbalanceType==JobLoadBalanceEnums.ROUNDROBIN.getCode()){

            loadBalance= new RoundRobinLoadBalance();

        }else if(loadbalanceType==JobLoadBalanceEnums.WEIGHT.getCode()){

            loadBalance= new WeightRoundRobinLoadBalance();

        }else {
            loadBalance= new RoundRobinLoadBalance();
        }

        jobMap.put(jobInfo.getId(),loadBalance);

        return loadBalance;
    }

}
