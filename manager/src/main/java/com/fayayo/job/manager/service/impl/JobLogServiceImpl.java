package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.entity.JobLog;
import com.fayayo.job.manager.repository.JobLogRepository;
import com.fayayo.job.manager.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

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

        log.info("{}新增任务日志,参数:{}", Constants.LOG_PREFIX,jobLog.toString());

        jobLogRepository.save(jobLog);

    }

    @Override
    public Page<JobLog> query(Pageable pageable, String jobDesc,String remoteIp) {

        Specification<JobLog> specification=new Specification<JobLog>() {
            @Override
            public Predicate toPredicate(Root<JobLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //获取列信息
                Expression<String> job_Desc = root.get("jobDesc");

                Expression<String> remote_Ip = root.get("remoteIp");

                List<Predicate> predicates = new ArrayList<Predicate>();

                //通过 CriteriaBuilder 来创建查询条件
                if (StringUtils.isNotEmpty(jobDesc)) {
                    predicates.add(criteriaBuilder.equal(job_Desc,jobDesc));
                }

                if (StringUtils.isNotEmpty(remoteIp)) {
                    predicates.add(criteriaBuilder.equal(remote_Ip,remoteIp));
                }

                query.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        };

        Page<JobLog> page=jobLogRepository.findAll(specification,pageable);

        return page;
    }


}
