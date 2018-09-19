package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author dalizu on 2018/9/18.
 * @version v1.0
 * @desc
 */
public interface JobLogService {

    void save(JobLog jobLog);

    Page<JobLog> query(Pageable pageable, String jobDesc);

}
