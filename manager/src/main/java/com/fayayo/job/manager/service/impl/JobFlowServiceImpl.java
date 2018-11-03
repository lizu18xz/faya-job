package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.util.KeyUtil;
import com.fayayo.job.entity.JobFlow;
import com.fayayo.job.entity.params.JobFlowParams;
import com.fayayo.job.manager.repository.JobFlowRepository;
import com.fayayo.job.manager.service.JobFlowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author dalizu on 2018/11/3.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class JobFlowServiceImpl implements JobFlowService {


    @Autowired
    private JobFlowRepository jobFlowRepository;


    @Override
    public JobFlow saveOrUpdate(JobFlowParams jobFlowParams) {
        JobFlow jobFlow=new JobFlow();
        String id=jobFlowParams.getId();
        if(StringUtils.isNotBlank(id)){//修改

            jobFlow=findOne(id);
            BeanUtils.copyProperties(jobFlowParams,jobFlow);
            jobFlowRepository.save(jobFlow);

        }else {//新增
            String keyId= KeyUtil.genUniqueKey();
            BeanUtils.copyProperties(jobFlowParams,jobFlow);
            jobFlow.setId(keyId);
            jobFlowRepository.save(jobFlow);
        }
        return jobFlow;
    }

    @Override
    public JobFlow findOne(String jobFlowId) {

        JobFlow jobFlow=jobFlowRepository.findById(jobFlowId).orElse(null);

        log.debug("{}查询单个任务执行器信息, 结果:{}", Constants.LOG_PREFIX, jobFlow);

        return jobFlow;
    }

    @Override
    public Page<JobFlow> query(Pageable pageable) {
        Page<JobFlow> page=jobFlowRepository.findAll(pageable);

        List<JobFlow> sortList=page.getContent();

        if(!CollectionUtils.isEmpty(sortList)){
            //按照seq字段排序 seq从小到大
            Collections.sort(sortList, new Comparator<JobFlow>() {
                @Override
                public int compare(JobFlow o1, JobFlow o2) {
                    return o1.getSeq() - o2.getSeq();
                }
            });
        }
        return new PageImpl<>(sortList,pageable,page.getTotalElements());
    }

    @Override
    public JobFlow findByName(String name) {
        return jobFlowRepository.findByName(name);
    }

    @Override
    public void deleteById(String id) {

        //先删除任务流下面的任务


        jobFlowRepository.deleteById(id);

    }

    public static void main(String[] args) {
        System.out.println(StringUtils.isNotBlank("x"));
        System.out.println(StringUtils.isNoneBlank(""));
        System.out.println(StringUtils.isNotEmpty(""));
        System.out.println(StringUtils.isNoneEmpty(""));
    }

}
