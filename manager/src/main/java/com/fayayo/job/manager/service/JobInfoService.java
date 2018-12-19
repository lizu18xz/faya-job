package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobInfoParams;
import com.fayayo.job.manager.vo.JobInfoVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc
 */
public interface JobInfoService {

     /**
      *@描述 新增job
      */

     void pauseJob(String jobId,String groupId);

     void resumeJob(String jobId,String groupId);

     void deleteJob(String jobId);

     /**
      *@描述 获取jobInfo
      */
     JobInfo findOne(String jobId);

     JobInfoVo findJobInfoVo(String jobId);

     Page<JobInfoVo>query(Pageable pageable,String executorType,Integer status);


     List<JobInfo>findByGroupId(Integer groupId);

     //任务流下面的任务
     JobInfo saveOrUpdate(JobInfoParams jobInfoParams);

     Page<JobInfo>queryByFlowId(Pageable pageable,String flowId);


     List<JobInfo> findByJobFlow(String flowId);
}
