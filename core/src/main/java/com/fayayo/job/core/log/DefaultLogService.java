package com.fayayo.job.core.log;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class DefaultLogService {

    private static String rootDir;

    //初始化执行器日志路径xxxx/2010-09-01
    public  void init(String rootPath){
        try {
            StringBuilder str=new StringBuilder();
            //判断执行器日志跟路径是否存在
            String currentDay= DateTimeUtil.dateToStr(new Date(),DateTimeUtil.DATE_PATTERN);
            rootPath=str.append(rootPath).append(File.separator).append(currentDay).toString();
            File file=new File(rootPath);
            //不存在就创建
            if(!file.exists()){
                file.mkdirs();
            }
            rootDir=rootPath;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writer(String msg) {
        if(rootDir!=null){
            append(msg);
        }else {
            log.error("执行器日志记录失败，请配置执行器日志目录");
        }
    }


    //将日志写入对应的文件 xxx/xxx/2018-01-01/xxxx.log
    private void append(String msg){

        FileOutputStream out=null;
        try {
            //获取日志ID构建文件
            String logName=getLogFileName();
            StringBuilder str=new StringBuilder();

            File file=new File(str.append(rootDir).
                                   append(File.separator).
                                   append(logName).toString());
            if(!file.exists()){
                file.createNewFile();
            }
            //写入日志信息到文件
            out=new FileOutputStream(file,true);
            out.write(msg.getBytes("utf-8"));
            out.flush();

        }catch (Exception e){
            e.printStackTrace();
            log.error("执行器日志写入失败,{}",e);
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("写入流关闭失败,{}",e);
                }
            }
        }

    }


    private String getLogFileName(){

        String logId=LogContextHolder.get();

        return logId + Constants.LOG_EXTENSION;
    }
    
}
