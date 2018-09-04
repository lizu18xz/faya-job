package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobGroup;
import com.fayayo.job.entity.params.JobGroupParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc 执行器service
 */
public interface JobGroupService {


    JobGroup addJobGroup(JobGroupParams jobGroupParams);

    /**
     *@描述 获取jobInfo
     */
    JobGroup findOne(Integer jobGroupId);


    Page<JobGroup>query(Pageable pageable);




}
