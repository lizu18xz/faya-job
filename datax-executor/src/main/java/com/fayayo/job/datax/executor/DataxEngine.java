package com.fayayo.job.datax.executor;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.common.util.ShellCall;
import com.fayayo.job.core.annotation.FayaService;
import com.fayayo.job.core.executor.bean.Result;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc Datax执行器入口
 */
@Slf4j
@FayaService(DataxEngine.class)
public class DataxEngine implements JobExecutorHandler {

    @Value("${faya-job.datax.config}")
    private String configHome;

    @Override
    public Result<?> run(JobInfoParam jobInfoParam) {
        try {
            log.info("datax task start....获取配置文件,调用shell脚本,启动datax");
            //获取datax的环境变量
            String dataxHome=System.getenv("DATAX_HOME");
            Integer id=jobInfoParam.getId();//任务唯一的id
            String json=id+".json";
            List<String>cmdList=new ArrayList<String>();
            cmdList.add("python ");
            cmdList.add(dataxHome+File.separator+"datax.py ");
            cmdList.add(configHome+File.separator+json);
            log.info("{}待执行命令:{}", Constants.LOG_PREFIX, StringUtils.join(cmdList," "));
            ShellCall.runCommand(cmdList);
        }catch (Exception e){
            log.info("datax任务执行失败,{}",e);
            return Result.error("任务执行失败，请联系管理员");
        }
        return Result.success("datax job success");
    }

}
