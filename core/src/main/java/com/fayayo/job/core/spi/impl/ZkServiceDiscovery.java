package com.fayayo.job.core.spi.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.spi.ServiceDiscovery;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
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
    public List<String> discover(String executorName) {

        //获取service节点
        String serviceNode=zkProperties.getRegisterPath()+"/"+executorName;

        List<String> list=zkCuratorClient.getChildNode(serviceNode);
        if(CollectionUtils.isEmpty(list)){
            log.info("{}找不到对应执行器:{} 的注册地址,请先确定服务是否部署",Constants.LOG_PREFIX,executorName);
            return null;
        }

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
