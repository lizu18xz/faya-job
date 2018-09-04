package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobInfoParams;
import com.fayayo.job.manager.repository.JobInfoRepository;
import com.fayayo.job.manager.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dalizu on 2018/8/8
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoRepository jobInfoRepository;

    @Autowired
    private JobSchedulerCore jobSchedulerCore;

    @Override
    public JobInfo addJob(JobInfoParams jobInfoParams) {

        //TODO 校验任务信息  获取任务配置相关的信息  比如依赖任务等

        //保存任务信息到数据库
        JobInfo jobInfo=new JobInfo();
        BeanUtils.copyProperties(jobInfoParams,jobInfo);
        jobInfo=jobInfoRepository.save(jobInfo);

        //把任务加入到quartz调度
        jobSchedulerCore.addJob(String.valueOf(jobInfo.getId()),String.valueOf(jobInfo.getJobGroup()),jobInfo.getCron(),jobInfo.getStartAt());
        return jobInfo;
    }

    @Override
    public JobInfo findOne(Integer jobId) {

        JobInfo jobInfo=jobInfoRepository.findById(jobId).orElse(null);

        log.debug("{}查询单个任务信息, 结果:{}", Constants.LOG_PREFIX, jobInfo);

        return jobInfo;
    }

    @Override
    public Page<JobInfo> query(Pageable pageable,String executorType,Integer status) {

        Specification<JobInfo> specification=new Specification<JobInfo>() {
            @Override
            public Predicate toPredicate(Root<JobInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //获取列信息
                Expression<String> executor_type = root.get("executorType");
                Expression<Integer> job_status = root.get("jobStatus");
                List<Predicate> predicates = new ArrayList<Predicate>();

                //通过 CriteriaBuilder 来创建查询条件
                if (StringUtils.isNotEmpty(executorType)) {
                    predicates.add(criteriaBuilder.equal(executor_type,executorType));
                }

                if (status!=null) {
                    predicates.add(criteriaBuilder.equal(job_status,status));
                }
                query.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        };

        Page<JobInfo> page=jobInfoRepository.findAll(specification,pageable);

        return page;
    }

}
