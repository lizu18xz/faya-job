package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.entity.JobConfig;
import com.fayayo.job.manager.repository.JobConfigRepository;
import com.fayayo.job.manager.service.JobConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dalizu on 2018/9/14.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class JobConfigServiceImpl implements JobConfigService {

    @Autowired
    private JobConfigRepository jobConfigRepository;



    public JobConfig save(JobConfig jobConfig){

        return jobConfigRepository.save(jobConfig);
    }



    @Override
    public JobConfig findOne(String jobId) {

        JobConfig jobConfig=jobConfigRepository.findById(jobId).orElse(null);

        log.debug("{}查询配置文件信息, 结果:{}", Constants.LOG_PREFIX, jobConfig);

        return jobConfig;
    }



}
