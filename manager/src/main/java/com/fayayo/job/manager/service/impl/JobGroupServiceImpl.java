package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.entity.JobGroup;
import com.fayayo.job.entity.params.JobGroupParams;
import com.fayayo.job.manager.repository.JobGroupRepository;
import com.fayayo.job.manager.service.JobGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class JobGroupServiceImpl implements JobGroupService{

    @Autowired
    private JobGroupRepository jobGroupRepository;


    @Override
    public JobGroup addJobGroup(JobGroupParams jobGroupParams) {

        JobGroup jobGroup=new JobGroup();
        BeanUtils.copyProperties(jobGroupParams,jobGroup);
        jobGroup=jobGroupRepository.save(jobGroup);

        return jobGroup;
    }

    @Override
    public JobGroup findOne(Integer jobGroupId) {

        JobGroup jobGroup=jobGroupRepository.findById(jobGroupId).orElse(null);

        log.debug("{}查询单个任务执行器信息, 结果:{}", Constants.LOG_PREFIX, jobGroup);

        return jobGroup;
    }

    @Override
    public Page<JobGroup> query(Pageable pageable) {

        Page<JobGroup> page=jobGroupRepository.findAll(pageable);

        return page;
    }


}
