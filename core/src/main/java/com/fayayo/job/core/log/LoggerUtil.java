package com.fayayo.job.core.log;

import com.fayayo.job.common.util.DateTimeUtil;
import com.fayayo.job.core.closable.Closable;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author dalizu on 2018/11/2.
 * @version v1.0
 * @desc 日志工具类,用于记录执行器  执行任务的日志(会设置 日志的id 作为文件名称,只能在调用执行器这个过程后使用此工具类打印日志!!!)
 */
public class LoggerUtil{


    private static DefaultLogService logService = new DefaultLogService();

    private static ArrayBlockingQueue<String> queue = null;

    private static volatile boolean running=false;

    private static Thread consumer=null;

    //初始化执行器日志路径xxxx/2010-09-01
    public static void init(String rootPath){
        logService.init(rootPath);
        queue=new ArrayBlockingQueue<String>(128);
        //启动消费者
        running=true;
        consumer=new Thread(new Consumer());
        consumer.start();
    }


    private static class Consumer implements Runnable{

        @Override
        public void run() {
            while (running){
                String logMsg= null;//阻塞等待日志
                try {
                    if((logMsg=queue.poll(1, TimeUnit.SECONDS))!=null){
                        //开始异步写日志
                        logService.writer(logMsg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //之前是同步打印会阻塞，修改为异步记录日志
    public static void info(String msg) {
        StringBuilder info=new StringBuilder();
        info.append(DateTimeUtil.dateToStr(new Date()))
                .append(",").append("【info】").append(" - ")
                .append(msg).append("\r\n");

        try {
            boolean b=queue.offer(info.toString());
            if(!b){//加入不进队列就从主线程写
                logService.writer(info.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void error(String msg) {
        StringBuilder error=new StringBuilder();
        error.append(DateTimeUtil.dateToStr(new Date()))
                .append(",").append("【error】").append(" - ")
                .append(msg).append("\r\n");

        try {
            boolean b=queue.offer(error.toString());
            if(!b){//加入不进队列就从主线程写
                logService.writer(error.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void stop() {
        running=false;
    }

    public static void main(String[] args) throws InterruptedException {
        //测试
        LoggerUtil.init("D:\\Test");
        for (int i=0;i<200;i++) {
            LoggerUtil.info("cccccc"+i);
        }
        Thread.sleep(6000);
        LoggerUtil.stop();
    }


}
