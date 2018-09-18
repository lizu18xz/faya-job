package com.fayayo.job.manager.repository;

import com.fayayo.job.entity.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dalizu on 2018/9/18.
 * @version v1.0
 * @desc
 */
public interface JobLogRepository extends JpaRepository<JobLog,String>,JpaSpecificationExecutor {




}
