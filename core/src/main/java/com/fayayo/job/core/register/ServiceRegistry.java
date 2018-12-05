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
     * @参数 服务名称(执行器标示)，服务地址
     */
    void register(String serviceName, String serviceAddress);

    /**
     * @描述 服务发现
     * @参数 服务名称
     */
    List<String> discover(String executorName);

    String getData(String path);

}
