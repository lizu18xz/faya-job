package com.fayayo.job.core.closable;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2018/8/16.
 * @version v1.0
 * @desc 关闭指定资源的配置
 */
@Configuration
public class ClosableConfig {


    @Bean
    public ServletListenerRegistrationBean listenerRegistrationBean() {
        ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(new ShutDownHookListener());
        return listenerRegistrationBean;
    }



}
