package com.fayayo.job.core.spi.impl;

import com.fayayo.job.core.spi.ServiceRegistry;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
import lombok.extern.slf4j.Slf4j;

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

     *@描述 服务注册

     *@参数  服务所属执行器 服务的地址

     */
    @Override
    public void register(String serviceName, String serviceAddress) {

        //创建服务的节点
        String serviceNode=zkProperties.getRegisterPath()+"/"+serviceName;

        zkCuratorClient.createPersistentNode(serviceNode);

        //创建地址的节点
        String addressNode=serviceNode+"/"+"address-";

        zkCuratorClient.createPhemeralEphemeralNode(addressNode+serviceAddress,serviceAddress);

    }

















}
