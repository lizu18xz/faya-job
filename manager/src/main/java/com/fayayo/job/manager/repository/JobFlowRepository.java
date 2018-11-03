package com.fayayo.job.manager.repository;

import com.fayayo.job.entity.JobFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dalizu on 2018/11/3.
 * @version v1.0
 * @desc
 */
public interface JobFlowRepository extends JpaRepository<JobFlow,String>,JpaSpecificationExecutor {

    JobFlow findByName(String name);

}
