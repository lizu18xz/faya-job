package com.fayayo.job.datax.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc 服务注册配置信息
 */
@Configuration
@ConfigurationProperties(prefix="faya-job.datax-executor")
@Getter
@Setter
public class ExecutorProperties {

    private String server;

    private Integer port;

    private Integer weight;//权重  轮训策略时候使用

}
