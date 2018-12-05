package com.fayayo.job.core.zookeeper;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.register.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {

    private ZKCuratorClient zkCuratorClient;

    private ZkProperties zkProperties;

    public ZkServiceRegistry(ZKCuratorClient zkCuratorClient, ZkProperties zkProperties) {
        this.zkCuratorClient = zkCuratorClient;
        this.zkProperties = zkProperties;
    }

    /**
     * @描述 服务注册
     * @参数 服务所属执行器 服务的地址
     */
    @Override
    public void register(String serviceName, String serviceAddress) {

        //创建服务的节点
        String serviceNode = zkProperties.getPath() + Constants.JOIN_SYMBOL + serviceName;

        zkCuratorClient.createPersistentNode(serviceNode);

        //创建地址的节点
        String addressNode = serviceNode + Constants.JOIN_SYMBOL + "address-";

        zkCuratorClient.createPhemeralEphemeralNode(addressNode + serviceAddress, serviceAddress);

    }

    @Override
    public List<String> discover(String executorName) {
        //获取service节点
        String serviceNode = zkProperties.getPath() + Constants.JOIN_SYMBOL + executorName;

        List<String> list = zkCuratorClient.getChildNode(serviceNode);
        if (CollectionUtils.isEmpty(list)) {
            log.info("{}找不到对应执行器:{} 的注册地址,请先确定服务是否部署", Constants.LOG_PREFIX, executorName);
            return null;
        }

        list = list.stream().map(e -> {
            return serviceNode + Constants.JOIN_SYMBOL + e;//加上前缀返回完整的路径
        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public String getData(String path) {
        return zkCuratorClient.getData(path);
    }


}
