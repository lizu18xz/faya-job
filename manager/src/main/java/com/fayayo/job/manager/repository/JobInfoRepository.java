package com.fayayo.job.manager.repository;

import com.fayayo.job.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc 数据库操作类
 */
public interface JobInfoRepository extends JpaRepository<JobInfo,Integer>,JpaSpecificationExecutor {



}
