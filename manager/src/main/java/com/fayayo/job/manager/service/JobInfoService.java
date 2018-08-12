package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobInfoParams;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc
 */
public interface JobInfoService {

     /**
      *@描述 新增job
      */

     JobInfo addJob(JobInfoParams jobInfoParams);

     /**
      *@描述 获取jobInfo
      */
     JobInfo findOne(Integer jobId);



}
