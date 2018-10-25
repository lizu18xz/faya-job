package com.fayayo.job.core.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.JobTypeEnums;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.common.util.DateTimeUtil;
import com.fayayo.job.core.executor.JobExecutor;
import com.fayayo.job.core.executor.result.LogResult;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import com.fayayo.job.core.thread.StandardThreadExecutor;
import com.fayayo.job.core.thread.StandardThreadManager;
import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.core.callback.CallBackParam;
import com.fayayo.job.core.callback.CallbackThread;
import com.fayayo.job.core.callback.task.HandlerTask;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * @author dalizu on 2018/8/18.
 * @version v1.0
 * @desc 具体执行任务的类
 */
@Slf4j
public class ExecutorRunImpl implements ExecutorRun {

    public static StandardThreadExecutor futureThread = StandardThreadManager.futureThread();

    private String server;

    private String logPath;

    public ExecutorRunImpl(String server,String logPath) {
        this.server = server;
        this.logPath = logPath;
    }


    /**
     * @描述 真正执行业务逻辑的地方, 在rpc server里面通过反射调用
     */
    @Override
    public Result<?> run(JobInfoParam jobInfo) {

        log.info("{}Start to Run Job,执行器类型:{},Params:{}", Constants.LOG_PREFIX, jobInfo.getExecutorType(),
                jobInfo.toString());

        String type = jobInfo.getJobType();
        JobExecutorHandler handler = null;
        if (type.equalsIgnoreCase(JobTypeEnums.BEAN.getName())) {
            //获取具体执行的服务
            handler = JobExecutor.getHandler();
        } else {
            log.info("{}暂未开放其他类型处理器!!!!!!", Constants.LOG_PREFIX);
        }

        //异步处理任务
        Future<?> future = futureThread.submit(new HandlerTask(handler, jobInfo));
        //把返回结果加入到回调处理
        CallbackThread.getInstance().pushFuture(new CallBackParam(jobInfo.getId(), future));

        return Result.success(server);//返回地址
    }


    /**
     * @描述 获取执行器产生的日志返回给管理端
     */
    @Override
    public Result<LogResult> log(String logId, long pointer) {

        log.info("Get log start:{},{}", logId, pointer);

        StringBuilder result=new StringBuilder();
        try {

            //获取完整的日志路径+文件名称  2018-10-24/154020971946352539.json-04_33_50.960.log
            String day= DateTimeUtil.dateToStr(new Date(),DateTimeUtil.DATE_PATTERN);
            String logFile=day+File.separator+logId+Constants.FILE_EXTENSION+Constants.LOG_EXTENSION;
            String path=logPath+File.separator+logFile;

            File file = new File(path);
            if (file == null) {
                return Result.success(new LogResult(0, "日志文件不存在"));
            }
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(pointer);//移动文件指针位置
            String line = null;

            while ((line = raf.readLine()) != null) {
                if (line.equals("")) {
                    continue;
                }
                line = new String(line.getBytes("ISO-8859-1"), "utf-8");
                result.append(line);
            }

            pointer = raf.getFilePointer();

        } catch (Exception e) {
            log.error("读取日志异常:{}", e.getMessage());
            e.printStackTrace();
        }

        return Result.success(new LogResult(pointer, result.toString()));
    }

}
