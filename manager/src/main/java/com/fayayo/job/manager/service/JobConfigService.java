package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobConfig;

/**
 * @author dalizu on 2018/9/14.
 * @version v1.0
 * @desc
 */
public interface JobConfigService {

    JobConfig save(JobConfig jobConfig);

    JobConfig findOne(String jobId);

}
