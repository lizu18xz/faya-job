package com.fayayo.job.datax.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/9/13.
 * @version v1.0
 * @desc 配置文件
 */
@Configuration
@ConfigurationProperties(prefix="faya-job.ftp-server")
@Getter
@Setter
public class FtpProperties {

    private String ip;

    private String username;

    private String password;

    private String serverPath;

}
