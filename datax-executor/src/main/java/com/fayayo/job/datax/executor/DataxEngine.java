package com.fayayo.job.datax.executor;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.params.JobInfoParam;
import com.fayayo.job.common.util.ShellCall;
import com.fayayo.job.core.annotation.FayaService;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc Datax执行器入口
 */
@Slf4j
@FayaService(DataxEngine.class)
public class DataxEngine extends JobExecutorHandler {

    @Value("${faya-job.datax.config}")
    private String configHome;

    private static final String BIN="bin";


    @Override
    public Result<?> run(JobInfoParam jobInfoParam) {
        try {
            log.info("datax task start....获取配置文件,调用shell脚本,启动datax");

            //获取datax的环境变量
            String dataxHome=System.getenv("DATAX_HOME");
            String jobId=jobInfoParam.getId();//任务唯一的id
            String json_name=String.format(Constants.DATAX_JOB_NAME_PREFIX,jobId,Constants.FILE_EXTENSION);//配置文件名称

            //获取配置文件信息到指定路径
            String content=jobInfoParam.getJobConfig();
            InputStream inputStream=IOUtils.toInputStream(content);
            String path=configHome+File.separator+json_name;
            FileOutputStream outputStream=new FileOutputStream(new File(path));
            IOUtils.copy(inputStream,outputStream);
            log.info("{}保存配置信息:{}", Constants.LOG_PREFIX, path);

            //命令组装调用
            List<String>cmdList=new ArrayList<String>();
            cmdList.add("python");
            cmdList.add(dataxHome+File.separator+BIN+File.separator+"datax.py");
            cmdList.add(path);
            log.info("{}待执行命令:{}", Constants.LOG_PREFIX, StringUtils.join(cmdList," "));
            ShellCall.runCommand(cmdList);
        }catch (Exception e){
            log.info("datax任务执行失败,{}",e);
            //TODO 删除文件

            return Result.error("任务执行失败，请联系管理员");
        }
        //TODO 删除文件
        return Result.success("datax job success");
    }

}
