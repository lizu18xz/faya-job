package com.fayayo.job.core.log;

import com.fayayo.job.common.util.DateTimeUtil;

import java.util.Date;

/**
 * @author dalizu on 2018/11/2.
 * @version v1.0
 * @desc 日志工具类,用于记录执行器  执行任务的日志(会设置 日志的id 作为文件名称,只能在调用执行器这个过程后使用此工具类打印日志!!!)
 */
public class LoggerUtil {

    private static DefaultLogService logService = new DefaultLogService();// 可以通过设置为不同logservice控制log行为。

    //初始化执行器日志路径xxxx/2010-09-01
    public static void init(String rootPath){
        logService.init(rootPath);
    }

    public static void info(String msg) {
        StringBuilder info=new StringBuilder();
        info.append(DateTimeUtil.dateToStr(new Date()))
                .append(",").append("【info】").append(" - ")
                .append(msg).append("\r\n");

        logService.info(info.toString());
    }

    public static void error(String msg) {
        StringBuilder error=new StringBuilder();
        error.append(DateTimeUtil.dateToStr(new Date()))
                .append(",").append("【error】").append(" - ")
                .append(msg).append("\r\n");
        logService.error(error.toString());
    }

}
