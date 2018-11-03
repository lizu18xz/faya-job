package com.fayayo.job.core.log;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/11/2.
 * @version v1.0
 * @desc  线程本地变量
 */
@Slf4j
public class LogContextHolder {


    private static final ThreadLocal<String>logHolder=new ThreadLocal<String>();

    public static void add(String logId){

        logHolder.set(logId);
    }


    public static String get(){
        return logHolder.get();
    }

    public static void remove(){
        log.info("remove threadLocal");
        logHolder.remove();
    }

}
