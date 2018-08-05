package com.fayayo.job.core.zookeeper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc zk配置
 */
@Configuration
public class ZkCuratorConfig {


    @Bean(initMethod = "init")
    public ZKCuratorClient zkCurator(){


        return new ZKCuratorClient();
    }


}
