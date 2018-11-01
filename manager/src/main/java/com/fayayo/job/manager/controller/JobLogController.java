package com.fayayo.job.manager.controller;

import com.fayayo.job.common.enums.JobExecutorTypeEnums;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import com.fayayo.job.core.executor.result.LogResult;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.JobLog;
import com.fayayo.job.manager.core.helper.LoggerHelper;
import com.fayayo.job.manager.service.JobInfoService;
import com.fayayo.job.manager.service.JobLogService;
import com.fayayo.job.manager.vo.JobLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dalizu on 2018/9/19.
 * @version v1.0
 * @desc
 */
@Slf4j
@RestController
@RequestMapping("/jobLog")
public class JobLogController {

    @Autowired
    private JobLogService jobLogService;

    @Autowired
    private JobInfoService jobInfoService;


    /**
     *@描述 分页条件查询
     *@返回值  List
     */
    @PostMapping("/list")
    public ResultVO<Page<JobLog>> list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                                       @RequestParam(value = "jobDesc",required = false)String jobDesc,
                                       @RequestParam(value = "remoteIp",required = false)String remoteIp){

        log.info("查询执行器,pageNum={},pageSize={}",page,size);
        Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of((page-1),size,sort);
        Page<JobLog> jobInfoPage=jobLogService.query(pageable,jobDesc,remoteIp);
        log.info("查询执行器,结果={}", jobInfoPage);
        return ResultVOUtil.success(jobInfoPage);
    }


     /**

       *@描述 滚动获取执行日志

       *@返回值  日志内容

     */
     @PostMapping("/loadLog")
     public ResultVO<?> loadLog(@RequestParam(value = "logId")String logId,
                                @RequestParam(value = "pointer",defaultValue = "0")long pointer){

         //获取日志的逻辑,发送请求到对应的机器，然后获取日志信息,获取完毕后,可以将日志内容保存到数据库中
         String executorAddress="";
         JobLog jobLog=jobLogService.findOne(logId);

         if(jobLog!=null){
             executorAddress=jobLog.getRemoteIp();
             String jobId=jobLog.getJobId();
             JobInfo jobInfo=jobInfoService.findOne(jobId);
             if(jobInfo!=null){
                 String executorType=jobInfo.getExecutorType();
                 if(!executorType.equals(JobExecutorTypeEnums.DATAX.getName())){
                     //return ResultVOUtil.error(ResultEnum.JOB_NOT_SUPPORT_LOG);
                     return ResultVOUtil.success(new LogResult(-1,ResultEnum.JOB_NOT_SUPPORT_LOG.getMessage()));
                 }
             }else {
                 throw new CommonException(ResultEnum.JOB_INFO_NOT_EXIST);
             }
         }else {
             throw new CommonException(ResultEnum.LOG_INFO_NOT_EXIST);
         }

         Result<LogResult>resultResult=LoggerHelper.getLogger(executorAddress,logId,pointer);

         return ResultVOUtil.success(resultResult.getData());
     }

    @PostMapping("/detail")
     public ResultVO<JobLogVo> detail(@RequestParam(value = "logId")String logId){

        JobLogVo jobLogVo=jobLogService.findJobLogVo(logId);

        return ResultVOUtil.success(jobLogVo);
     }


}
