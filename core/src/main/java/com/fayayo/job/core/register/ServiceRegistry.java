package com.fayayo.job.core.register;

import java.util.List;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc
 */
public interface ServiceRegistry {


    /**
     * @描述 服务注册
     */
    void register(String serviceName, String serviceAddress);

    /**
     * @描述 服务发现
     */
    List<String> discover(String executorName);

    /**
     * @描述 获取节点的值
     */
    String getData(String path);


    /**
     * @描述 删除节点
     */
    void deleteNode(String path);

}
