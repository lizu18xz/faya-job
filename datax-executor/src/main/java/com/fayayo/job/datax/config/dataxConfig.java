package com.fayayo.job.datax.config;

import com.fayayo.job.core.executor.JobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc dataxExecutor  初始化类
 */
@Configuration
public class dataxConfig {

    @Autowired
    private ExecutorProperties executorProperties;


    @Bean
    public JobExecutor jobExecutor(){

        return new JobExecutor(executorProperties.getServer(),
                executorProperties.getPort(),executorProperties.getWeight());

    }


}
