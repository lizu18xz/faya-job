package com.fayayo.job.manager.core.cluster.support;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.common.util.EnumUtil;
import com.fayayo.job.core.extension.ExtensionLoader;
import com.fayayo.job.core.service.ServiceDiscovery;
import com.fayayo.job.core.service.impl.ZkServiceDiscovery;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
import com.fayayo.job.manager.config.SpringHelper;
import com.fayayo.job.manager.core.cluster.HaStrategy;
import com.fayayo.job.manager.core.cluster.LoadBalance;
import com.fayayo.job.manager.core.cluster.ha.HaStrategyEnums;
import com.fayayo.job.manager.core.route.JobRouteExchange;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dalizu on 2018/8/17.
 * @version v1.0
 * @desc 构造cluster
 */
@Slf4j
public class ClusterSupport {

    /**
     * @描述 构造cluster
     */
    public static Cluster buildClusterSupport(JobInfoParam jobInfo) {
        //获取ha的实现
        Integer haCode = jobInfo.getJobHa();
        String haStrategyName = EnumUtil.getByCode(haCode, HaStrategyEnums.class).getDesc() == null
                ? "failover" : EnumUtil.getByCode(haCode, HaStrategyEnums.class).getDesc();//默认值
        HaStrategy haStrategy = ExtensionLoader.getExtensionLoader(HaStrategy.class).getExtension(haStrategyName);
        //获取loadbalance的实现
        LoadBalance loadBalance = getLoadBalance(jobInfo);
        Cluster cluster = new ClusterSpi(haStrategy, loadBalance);
        //设置重试次数
        cluster.setRetries(jobInfo.getRetries());

        return cluster;
    }

    /**
     * @描述 获取负载策略
     */
    private static LoadBalance getLoadBalance(JobInfoParam jobInfoParam) {

        //先从缓存获取ip地址列表，减少每次去zk查询的次数,有新执行器加入时候会主动刷新缓存
        String executorName = jobInfoParam.getJobGroupName();

        List<String> addressList = getExecutorAddress(executorName);
        log.info("{}执行器名称:{},服务地址列表:【{}】,jobid:{}", Constants.LOG_PREFIX, executorName, StringUtils.join(addressList, ","),
                    jobInfoParam.getId());

        //获取负载均衡的策略  +  Ha策略  然后对选择的机器发送请求任务
        JobRouteExchange jobRouteExchange = new JobRouteExchange(addressList);
        LoadBalance loadBalance = jobRouteExchange.getLoadBalance(jobInfoParam);
        log.info("{}服务负载策略:{}", Constants.LOG_PREFIX, loadBalance.getClass().getSimpleName());
        return loadBalance;
    }


    /**
     * @描述 构造获取执行器日志的cluster
     */
    public static Cluster buildLogClusterSupport(String executorAddress) {
        //获取ha的实现 默认使用快速失败
        HaStrategy haStrategy = ExtensionLoader.getExtensionLoader(HaStrategy.class).getExtension(
                HaStrategyEnums.FAIL_FAST.getDesc()
        );

        //一个日志只存在固定的一台机器的情况,默认直接轮训就可以
        //TODO 判断执行器地址是否可连接
        JobRouteExchange jobRouteExchange = new JobRouteExchange(Lists.newArrayList(executorAddress));
        LoadBalance loadBalance = jobRouteExchange.getLogLoadBalance();

        Cluster cluster = new ClusterSpi(haStrategy, loadBalance);
        //设置重试次数
        cluster.setRetries(0);

        return cluster;
    }


    private static List getExecutorAddress(String executorName) {

        //从zk中获取 服务ip地址列表
        ZKCuratorClient zkCuratorClient = SpringHelper.popBean(ZKCuratorClient.class);
        ZkProperties zkProperties = SpringHelper.popBean(ZkProperties.class);

        ServiceDiscovery serviceDiscovery = new ZkServiceDiscovery(zkCuratorClient, zkProperties);

        List<String> list = serviceDiscovery.discover(executorName);//根据所属执行器查询ip地址
        if (CollectionUtils.isEmpty(list)) {
            throw new CommonException(ResultEnum.EXECUTOR_ADDRESS_NOT_EXIST);
        }
        //获取具体的ip
        List addressList = list.stream().map(e -> {
            return zkCuratorClient.getData(e);
        }).collect(Collectors.toList());

        return addressList;
    }

}
