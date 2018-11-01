package com.fayayo.job.manager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * ApplicationContextAware  实现后 bean会执行setApplicationContext 自动注入applicationContext
 *
 */
@Slf4j
@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.setApplicationContext(applicationContext);
        //执行一些初始化的操作
    }
}