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

     JobInfo addJob(JobInfoParams jobInfoParams);

     JobInfo updateJob(JobInfoParams jobInfoParams);

     void pauseJob(String jobId,String groupId);

     void resumeJob(String jobId,String groupId);

     void deleteJob(String jobId,String groupId);

     /**
      *@描述 获取jobInfo
      */
     JobInfo findOne(String jobId);

     JobInfoVo findJobInfoVo(String jobId);

     Page<JobInfo>query(Pageable pageable,String executorType,Integer status);


     List<JobInfo>findByGroupId(Integer groupId);

}
