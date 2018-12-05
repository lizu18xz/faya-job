package com.fayayo.job.manager.core.helper;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.JobExecutorTypeEnums;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.common.util.EnumUtil;
import com.fayayo.job.common.util.KeyUtil;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.core.extension.ExtensionLoader;
import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.entity.JobConfig;
import com.fayayo.job.entity.JobGroup;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.JobLog;
import com.fayayo.job.manager.config.SpringHelper;
import com.fayayo.job.manager.core.cluster.ha.HaStrategyEnums;
import com.fayayo.job.manager.core.cluster.loadbalance.JobLoadBalanceEnums;
import com.fayayo.job.manager.core.cluster.support.Cluster;
import com.fayayo.job.manager.core.cluster.support.ClusterSupport;
import com.fayayo.job.manager.core.proxy.ProxyFactory;
import com.fayayo.job.manager.core.proxy.impl.JdkProxyFactory;
import com.fayayo.job.manager.service.JobConfigService;
import com.fayayo.job.manager.service.JobGroupService;
import com.fayayo.job.manager.service.JobInfoService;
import com.fayayo.job.manager.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author dalizu on 2018/8/10.
 * @version v1.0
 * @desc 核心处理类  集群{负载 高可用} 发送请求
 */
@Slf4j
public class TriggerHelper {

     /**
       *@描述 处理job的业务

       *@参数  job的唯一标示
     */
    public static void Trigger(String jobId){

        //获取job的详细信息 ,执行器信息
        JobInfoService jobInfoService=SpringHelper.popBean(JobInfoService.class);
        JobInfo jobInfo=jobInfoService.findOne(jobId);
        if(jobInfo==null){
            throw new CommonException(ResultEnum.JOB_NOT_EXIST);
        }

        JobInfoParam jobInfoParam=new JobInfoParam();
        BeanUtils.copyProperties(jobInfo,jobInfoParam);
        JobGroupService jobGroupService=SpringHelper.popBean(JobGroupService.class);
        JobGroup jobGroup=jobGroupService.findOne(jobInfoParam.getJobGroup());
        jobInfoParam.setJobGroupName(jobGroup.getName());

        String jobLogId=KeyUtil.genUniqueKey();
        jobInfoParam.setLogId(jobLogId);

        //build cluster  配置机器的ha和选择服务的策略
        Cluster cluster=ClusterSupport.buildClusterSupport(jobInfoParam);

        //获取代理类
        ExecutorRun executorSpi=getExecutorSpi(cluster);

        //判断任务类型 是否需要额外配置文件
        String jobExecutorType=jobInfoParam.getExecutorType();
        if(jobExecutorType.equals(JobExecutorTypeEnums.DATAX.getName())){
            //DATAX 数据交换 获取配置文件信息
            JobConfigService jobConfigService=SpringHelper.popBean(JobConfigService.class);
            JobConfig jobConfig=jobConfigService.findOne(jobId);
            jobInfoParam.setJobConfig(jobConfig.getContent());
        }

        //保存执行记录,根据jobId查询一个任务执行的记录
        JobLogService jobLogService=SpringHelper.popBean(JobLogService.class);
        JobLog jobLog=new JobLog();
        jobLog.setId(jobLogId);
        jobLog.setJobId(jobId);
        jobLog.setJobDesc(jobInfo.getJobDesc());
        jobLog.setLoadBalance(EnumUtil.getByCode(jobInfo.getJobLoadBalance(),JobLoadBalanceEnums.class).getDesc());
        jobLog.setHa(EnumUtil.getByCode(jobInfo.getJobHa(),HaStrategyEnums.class).getDesc());
        jobLog.setRemoteIp("");
        jobLogService.save(jobLog);
        Result<?> result=executorSpi.run(jobInfoParam);//jdkProxy
        jobLog.setRemoteIp(result.getData().toString());
        jobLogService.save(jobLog);

         //获取任务的执行结果
        log.info("{}job success:{}", Constants.LOG_PREFIX,result);

    }

    /**
     * @描述 获取代理类
     */
    private static ExecutorRun getExecutorSpi(Cluster cluster) {
        //TODO 暂时只有一种代理，后期增加可以把类型通过参数传入进来
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(Constants.PROXY_JDK);
        return proxyFactory.getProxy(ExecutorRun.class, cluster);
    }


}
