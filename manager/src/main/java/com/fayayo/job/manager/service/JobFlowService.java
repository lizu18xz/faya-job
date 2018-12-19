package com.fayayo.job.manager.service;

import com.fayayo.job.entity.JobFlow;
import com.fayayo.job.entity.params.JobFlowParams;
import com.fayayo.job.manager.vo.JobFlowVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author dalizu on 2018/11/3.
 * @version v1.0
 * @desc
 */
public interface JobFlowService {

    JobFlow saveOrUpdate(JobFlowParams jobFlowParams);

    /**
     *@描述 获取jobInfo
     */
    JobFlow findOne(String jobFlowId);

    JobFlowVo findById(String jobFlowId);

    Page<JobFlowVo> query(Pageable pageable);

    JobFlow findByName(String name);

    void deleteById(String id);

    void upOrDown(String id,Integer status);

}
