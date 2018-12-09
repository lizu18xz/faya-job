package com.fayayo.job.manager.controller;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import com.fayayo.job.entity.JobFlow;
import com.fayayo.job.entity.params.JobFlowParams;
import com.fayayo.job.manager.service.JobFlowService;
import com.fayayo.job.manager.vo.JobFlowVo;
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
 * @author dalizu on 2018/11/3.
 * @version v1.0
 * @desc
 */
@Slf4j
@RestController
@RequestMapping("/jobFlow")
public class JobFlowController {


    @Autowired
    private JobFlowService jobFlowService;

    /**
     *@描述 新增执行器
     */
    @PostMapping("/save")
    public ResultVO add(@Valid JobFlowParams jobFlowParams, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CommonException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        log.info("{}新增任务流,参数:{}", Constants.LOG_PREFIX,jobFlowParams);
        JobFlow jobFlow=jobFlowService.saveOrUpdate(jobFlowParams);
        return ResultVOUtil.success();
    }

    /**
     *@描述 执行器列表展示
     *@返回值  List
     */
    @PostMapping("/list")
    public ResultVO<Page<JobFlowVo>>list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                       @RequestParam(value = "size",defaultValue = "10")Integer size){
        log.info("查询执行器,pageNum={},pageSize={}",page,size);
        Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of((page-1),size,sort);
        Page<JobFlowVo>jobFlowPage=jobFlowService.query(pageable);
        log.info("查询执行器,结果={}", jobFlowPage);
        return ResultVOUtil.success(jobFlowPage);
    }

    /**
     *@描述 修改执行器
     */
    @PostMapping("/update")
    public ResultVO updateJob(@Valid  JobFlowParams jobFlowParams, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new CommonException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        log.info("{}修改执行器,参数:{}", Constants.LOG_PREFIX,jobFlowParams);
        JobFlow jobFlow=jobFlowService.saveOrUpdate(jobFlowParams);
        return ResultVOUtil.success();
    }


    @PostMapping("/detail")
    public ResultVO<JobFlowVo> detail(@RequestParam(value = "id") String id){

        JobFlowVo jobFlowVo=jobFlowService.findById(id);

        return ResultVOUtil.success(jobFlowVo);
    }


    @PostMapping("/delete")
    public ResultVO<JobFlow> delete(@RequestParam(value = "id") String id){

        jobFlowService.deleteById(id);

        return ResultVOUtil.success();
    }


    @PostMapping("/upOrDown")
    public ResultVO<JobFlow> upOrDown(@RequestParam(value = "id") String id,
                                      @RequestParam(value = "status") Integer status){

        jobFlowService.upOrDown(id,status);

        return ResultVOUtil.success();
    }


}
