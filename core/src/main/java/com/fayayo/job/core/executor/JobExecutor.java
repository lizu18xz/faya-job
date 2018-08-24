package com.fayayo.job.core.executor;

import com.fayayo.job.core.spi.impl.ZkServiceRegistry;
import com.fayayo.job.core.transport.NettyServer;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.CountDownLatch;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc job执行器
 */
@Slf4j
public class JobExecutor implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    private ZKCuratorClient zkCuratorClient;

    @Autowired
    private ZkProperties zkProperties;

    private String server;

    private Integer port;

    private Integer weight;

    private String name;

    public JobExecutor(String server, Integer port, Integer weight, String name) {
        this.server = server;
        this.port = port;
        this.weight = weight;
        this.name = name;
        if (this.weight == null) {
            this.weight = 1;//如果不写默认全是1
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        log.info("执行器准备......:{}", server);
        //然后启动这个服务端，准备接收请求
        CountDownLatch countDownLatch=new CountDownLatch(1);//阻塞线程
        NettyServer nettyServer = new NettyServer(port);
        nettyServer.start(countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("服务端启动完毕,开始注册到服务中心");
        //服务端启动成功后，注册此执行器的这个服务到zk
        ZkServiceRegistry zkServiceRegistry = new ZkServiceRegistry(zkCuratorClient, zkProperties);
        String serviceAddress = new StringBuilder().
                append(server).append(":").
                append(port).append(":").
                append(weight).toString();
        zkServiceRegistry.register(name, serviceAddress);



    }


}
