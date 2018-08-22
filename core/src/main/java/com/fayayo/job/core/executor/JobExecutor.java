package com.fayayo.job.core.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc job执行器
 */
@Slf4j
public class JobExecutor implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    private String server;

    private Integer port;

    private Integer weight;

    public JobExecutor(String server, Integer port, Integer weight) {
        this.server = server;
        this.port = port;
        this.weight = weight;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
        log.info("执行器准备......:{}",server);

        //然后启动这个服务端，准备接收请求


        //服务端启动成功后，注册此执行器的这个服务到zk

        //接收到请求后 根据相关参数信息调用真正的执行方法

    }



}
