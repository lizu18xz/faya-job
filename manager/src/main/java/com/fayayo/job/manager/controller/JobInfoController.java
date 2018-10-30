package com.fayayo.job.manager.controller;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import com.fayayo.job.entity.JobGroup;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobInfoParams;
import com.fayayo.job.manager.core.helper.TriggerHelper;
import com.fayayo.job.manager.service.JobInfoService;
import com.fayayo.job.manager.vo.JobInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/job")
public class JobInfoController {

    @Autowired
    private JobInfoService jobInfoService;

    /**
    *@描述 新增任务
    */
    @PostMapping("/save")
    public ResultVO addJob(@Valid JobInfoParams jobInfoParams, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CommonException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        log.info("{}新增任务,参数:{}",Constants.LOG_PREFIX,jobInfoParams);
        JobInfo jobInfo=jobInfoService.addJob(jobInfoParams);
        return ResultVOUtil.success();
    }


    @PostMapping("/editor")
    public ResultVO editorJob(@Valid JobInfoParams jobInfoParams, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CommonException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        log.info("{}修改任务,参数:{}",Constants.LOG_PREFIX,jobInfoParams);
        JobInfo jobInfo=jobInfoService.updateJob(jobInfoParams);
        return ResultVOUtil.success();
    }


    /**
     *@描述 暂停任务
     */
    @PostMapping("/pause")
    public ResultVO pause(@RequestParam("jobId")String jobId,
                          @RequestParam("jobGroup")String jobGroup){
        jobInfoService.pauseJob(jobId,jobGroup);
        return ResultVOUtil.success();
    }

    /**
     *@描述 唤醒任务
     */
    @PostMapping("/resume")
    public ResultVO resume(@RequestParam("jobId")String jobId,
                           @RequestParam("jobGroup")String jobGroup){
        jobInfoService.resumeJob(jobId,jobGroup);
        return ResultVOUtil.success();
    }

    /**
     *@描述 删除任务
     */
    @PostMapping("/delete")
    public ResultVO delete(@RequestParam("jobId")String jobId,
                           @RequestParam("jobGroup")String jobGroup){
        jobInfoService.deleteJob(jobId,jobGroup);
        return ResultVOUtil.success();
    }

    /**
     *@描述 分页条件查询
     *@返回值  List
     */
    @PostMapping("/list")
    public ResultVO<Page<JobInfo>> list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                        @RequestParam(value = "size",defaultValue = "10")Integer size,
                                        @RequestParam(value = "executorType",required = false)String executorType,
                                        @RequestParam(value = "status",required = false)Integer status){

        log.info("查询执行器,pageNum={},pageSize={}",page,size);
        Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of((page-1),size,sort);
        Page<JobInfo> jobInfoPage=jobInfoService.query(pageable,executorType,status);
        log.info("查询执行器,结果={}", jobInfoPage);
        return ResultVOUtil.success(jobInfoPage);

    }


     /**

       *@描述 手动执行一次任务

       *@参数  服务名称

     */
    @PostMapping("/trigger")
    public ResultVO trigger(@RequestParam("jobId")String jobId){

        TriggerHelper.Trigger(jobId);

        return ResultVOUtil.success();
    }

    /**
     *@描述 任务详情
     */
    @PostMapping("/detail")
    public ResultVO delete(@RequestParam("jobId")String jobId){
        JobInfoVo jobInfoVo=jobInfoService.findJobInfoVo(jobId);
        return ResultVOUtil.success(jobInfoVo);
    }

}
