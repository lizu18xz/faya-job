package com.fayayo.job.demo.config;

import com.fayayo.job.core.executor.JobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/10/15.
 * @version v1.0
 * @desc
 */
@Configuration
public class DemoConfig {

    @Autowired
    private ExecutorProperties properties;


    @Bean(destroyMethod = "close")
    public JobExecutor jobExecutor(){

        return new JobExecutor(properties.getServer(),
                properties.getPort(),properties.getWeight(),properties.getName(),
                properties.getMainClass());

    }


}
