package com.fayayo.job.core.spi;

import java.util.List;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc
 */
public interface ServiceDiscovery {


    /**

     *@描述 服务发现

     *@参数  服务名称

     *@返回值  服务地址

     *@创建人  dalizu

     *@创建时间  2018/8/4

     */
    List<String> discover(String executorName);

    String getData(String path);

}
