package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobGroup;
import com.fayayo.job.entity.params.JobGroupParams;
import com.fayayo.job.manager.vo.JobGroupVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc 执行器service
 */
public interface JobGroupService {


    JobGroup saveOrUpdate(JobGroupParams jobGroupParams);

    /**
     *@描述 获取jobInfo
     */
    JobGroup findOne(Integer jobGroupId);


    Page<JobGroupVo>query(Pageable pageable);


    JobGroup findByName(String name);

    void deleteById(Integer id);

}
