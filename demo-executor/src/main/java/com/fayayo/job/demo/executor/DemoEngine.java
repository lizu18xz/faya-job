package com.fayayo.job.demo.executor;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.core.annotation.FayaService;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.core.log.LoggerUtil;

/**
 * @author dalizu on 2018/10/15.
 * @version v1.0
 * @desc 任务启动模板
 */
@FayaService(DemoEngine.class)
public class DemoEngine extends JobExecutorHandler {
    @Override
    public Result<?> run(JobInfoParam jobInfoParam) {

        LoggerUtil.info("demo task start....运行DemoEngine！！！！！！");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LoggerUtil.info("demo task start....运行DemoEngine END ！！！！！！");
        return Result.success("demo job success");
    }
}
