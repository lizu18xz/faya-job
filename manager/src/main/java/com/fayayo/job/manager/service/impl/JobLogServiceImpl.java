package com.fayayo.job.manager.service.impl;

import com.fayayo.job.entity.JobLog;
import com.fayayo.job.manager.repository.JobLogRepository;
import com.fayayo.job.manager.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dalizu on 2018/9/18.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class JobLogServiceImpl implements JobLogService {


    @Autowired
    private JobLogRepository jobLogRepository;

    public void save(JobLog jobLog){
        jobLogRepository.save(jobLog);
    }




}
