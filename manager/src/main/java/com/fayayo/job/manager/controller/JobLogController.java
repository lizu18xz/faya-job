package com.fayayo.job.manager.controller;

import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import com.fayayo.job.entity.JobLog;
import com.fayayo.job.manager.service.JobLogService;
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

    /**
     *@描述 分页条件查询
     *@返回值  List
     */
    @PostMapping("/list")
    public ResultVO<Page<JobLog>> list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                       @RequestParam(value = "size",defaultValue = "10")Integer size,
                                       @RequestParam(value = "jobDesc",required = false)String jobDesc){

        log.info("查询执行器,pageNum={},pageSize={}",page,size);
        Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of((page-1),size,sort);
        Page<JobLog> jobInfoPage=jobLogService.query(pageable,jobDesc);
        log.info("查询执行器,结果={}", jobInfoPage);
        return ResultVOUtil.success(jobInfoPage);
    }


}
