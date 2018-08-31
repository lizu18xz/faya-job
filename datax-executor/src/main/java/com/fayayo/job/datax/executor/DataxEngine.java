package com.fayayo.job.datax.executor;

import com.fayayo.job.core.annotation.FayaService;
import com.fayayo.job.core.executor.bean.Result;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc Datax执行器入口
 */
@Slf4j
@FayaService(DataxEngine.class)
public class DataxEngine implements JobExecutorHandler {

    @Override
    public Result<?> run() {
        log.info("datax task start....获取配置文件,调用shell脚本,启动datax");


        return Result.success("datax job success");
    }

}
