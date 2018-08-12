package com.fayayo.job.manager.core.trigger;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.spi.ServiceDiscovery;
import com.fayayo.job.core.spi.impl.ZkServiceDiscovery;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.manager.config.SpringHelper;
import com.fayayo.job.manager.core.cluster.loadbalance.Endpoint;
import com.fayayo.job.manager.core.route.JobRouteExchange;
import com.fayayo.job.manager.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dalizu on 2018/8/10.
 * @version v1.0
 * @desc
 */
@Slf4j
public class TriggerHelper {


     /**
       *@描述 处理job的业务

       *@参数  job的唯一标示
     */
    public static void Trigger(Integer jobId){

        //获取job的详细信息  或者执行器信息
        JobInfoService jobInfoService=SpringHelper.popBean(JobInfoService.class);
        JobInfo jobInfo=jobInfoService.findOne(jobId);
        if(jobInfo==null){
            throw new CommonException(ResultEnum.JOB_NOT_EXIST);
        }

        //从zk中获取 服务ip地址列表
        ZKCuratorClient zkCuratorClient=SpringHelper.popBean(ZKCuratorClient.class);
        ZkProperties zkProperties=SpringHelper.popBean(ZkProperties.class);

        ServiceDiscovery serviceDiscovery=new ZkServiceDiscovery(zkCuratorClient,zkProperties);
        List<String>list=serviceDiscovery.discover(String.valueOf(jobInfo.getJobGroup()));//根据所属执行器查询ip地址
        if(CollectionUtils.isEmpty(list)){
            throw new CommonException(ResultEnum.JOB_NOT_FIND_ADDRESS);
        }
        //log.info("获取服务地址列表,jobid:{},groupId:{},addressList:{}",jobId,jobInfo.getJobGroup(), StringUtils.join(list,","));
        //获取具体的ip
        List<String>addressList=list.stream().map(e->{
            return zkCuratorClient.getData(e);
        }).collect(Collectors.toList());
        log.info("获取服务地址列表,jobid:{},groupId:{},addressList:{}",jobId,jobInfo.getJobGroup(), StringUtils.join(addressList,","));

        //获取负载均衡的策略  +  Ha策略  然后对选择的机器发送请求任务
        JobRouteExchange jobRouteExchange=new JobRouteExchange(addressList);
        Endpoint endpoint=jobRouteExchange.getLoadBalance(jobInfo);
        log.info("服务执行地址信息:{}",endpoint.toString());
        //获取代理类--->执行方法

    }












}
