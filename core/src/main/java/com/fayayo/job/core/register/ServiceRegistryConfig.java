package com.fayayo.job.core.register;

import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
import com.fayayo.job.core.zookeeper.ZkServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/12/5.
 * @version v1.0
 * @desc 服务注册发现配置, 默认zk, 可以自定义
 */
@Slf4j
@Configuration
public class ServiceRegistryConfig {

    @Autowired
    private ZkProperties zkProperties;

    @Bean
    @ConditionalOnMissingBean(name = "serviceRegistry")
    @ConditionalOnProperty(value = {"faya-job.register.server", "faya-job.register.path"})
    public ServiceRegistry serviceRegistry() {

        log.info("init default register");

        ZKCuratorClient zkCuratorClient = new ZKCuratorClient(zkProperties);
        zkCuratorClient.init();
        ZkServiceRegistry zkServiceRegistry = new ZkServiceRegistry(zkCuratorClient, zkProperties);
        return zkServiceRegistry;

    }

}
