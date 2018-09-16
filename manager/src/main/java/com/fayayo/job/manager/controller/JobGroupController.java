package com.fayayo.job.manager.controller;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import com.fayayo.job.entity.JobGroup;
import com.fayayo.job.entity.params.JobGroupParams;
import com.fayayo.job.manager.service.JobGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc group api
 */
@Slf4j
@RestController
@RequestMapping("/jobGroup")
public class JobGroupController {

    @Autowired
    private JobGroupService jobGroupService;

    /**
     *@描述 新增执行器
     */
    @PostMapping("/save")
    public ResultVO addJob(@Valid JobGroupParams jobGroupParams, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CommonException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        log.info("{}新增任务,参数:{}", Constants.LOG_PREFIX,jobGroupParams);
        JobGroup jobGroup=jobGroupService.addJobGroup(jobGroupParams);
        return ResultVOUtil.success();
    }

     /**
       *@描述 执行器列表展示
       *@返回值  List
     */
     @PostMapping("/list")
     public ResultVO<Page<JobGroup>>list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                   @RequestParam(value = "size",defaultValue = "10")Integer size){
         log.info("查询执行器,pageNum={},pageSize={}",page,size);
         Sort sort=new Sort(Sort.Direction.DESC,"createTime");
         Pageable pageable = PageRequest.of((page-1),size,sort);
         Page<JobGroup>jobGroupPage=jobGroupService.query(pageable);
         log.info("查询执行器,结果={}", jobGroupPage);
         return ResultVOUtil.success(jobGroupPage);
     }



}
