package com.fayayo.job.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc 注解标志
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface FayaService {

    /**
     * 服务接口类
     */
    Class<?>value();

}
