package com.fayayo.job.manager.repository;

import com.fayayo.job.entity.JobGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc 任务执行器数据库操作
 */
public interface JobGroupRepository extends JpaRepository<JobGroup,Integer>,JpaSpecificationExecutor {


}
