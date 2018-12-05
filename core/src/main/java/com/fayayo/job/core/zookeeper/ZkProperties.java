package com.fayayo.job.core.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc
 */
@Configuration
@ConfigurationProperties(prefix="faya-job.register")
@Getter
@Setter
public class ZkProperties {


    private String server;


    private String path;


}
