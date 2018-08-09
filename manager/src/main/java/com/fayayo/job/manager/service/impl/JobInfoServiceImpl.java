package com.fayayo.job.manager.service.impl;

import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobInfoParams;
import com.fayayo.job.manager.repository.JobInfoRepository;
import com.fayayo.job.manager.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        //jobSchedulerCore.addJob(jobInfo.getId(),jobInfo.getJobGroup(),jobInfo.getCron(),jobInfo.getStartAt());

        return jobInfo;
    }

}
