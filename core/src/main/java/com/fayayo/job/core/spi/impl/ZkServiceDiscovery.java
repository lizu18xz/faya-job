package com.fayayo.job.core.spi.impl;

import com.fayayo.job.core.spi.ServiceDiscovery;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc
 */
public class ZkServiceDiscovery implements ServiceDiscovery {


    private ZKCuratorClient zkCuratorClient;

    private ZkProperties zkProperties;

    public ZkServiceDiscovery(ZKCuratorClient zkCuratorClient, ZkProperties zkProperties) {
        this.zkCuratorClient = zkCuratorClient;
        this.zkProperties = zkProperties;
    }


    /**

     *@描述 服务发现

     *@参数  服务名称

     *@返回值  服务的地址

     */
    @Override
    public List<String> discover(String serviceName) {

        //获取service节点
        String serviceNode=zkProperties.getRegisterPath()+"/"+serviceName;

        List<String> list=zkCuratorClient.getChildNode(serviceNode);

        list=list.stream().map(e->{
            return serviceNode+"/"+e;//加上前缀返回完整的路径
        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public String getData(String path) {
        return zkCuratorClient.getData(path);
    }


}
