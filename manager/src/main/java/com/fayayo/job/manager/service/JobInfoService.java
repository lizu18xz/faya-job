package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobInfoParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

     void pauseJob(String jobId,String groupId);

     void resumeJob(String jobId,String groupId);

     void deleteJob(String jobId,String groupId);

     /**
      *@描述 获取jobInfo
      */
     JobInfo findOne(String jobId);


     Page<JobInfo>query(Pageable pageable,String executorType,Integer status);

}
