package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.JobExecutorTypeEnums;
import com.fayayo.job.common.enums.JobTypeEnums;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.util.KeyUtil;
import com.fayayo.job.entity.JobConfig;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobInfoParams;
import com.fayayo.job.manager.repository.JobInfoRepository;
import com.fayayo.job.manager.service.FileService;
import com.fayayo.job.manager.service.JobConfigService;
import com.fayayo.job.manager.service.JobInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.InputStream;
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

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private FileService fileService;

    @Value("${faya-job.upload.tempPath}")
    private String path;

     /**
       *@描述 新增任务
     */
    @Transactional
    @Override
    public JobInfo addJob(JobInfoParams jobInfoParams) {

        //TODO 校验任务信息  获取任务配置相关的信息  比如依赖任务等

        String keyId= KeyUtil.genUniqueKey();//获取主键ID

        String jobExecutorType=jobInfoParams.getExecutorType();
        if(jobExecutorType.equals(JobExecutorTypeEnums.DATAX.getName())){
            String jobConfigBody=jobInfoParams.getJobConfig();
            if(StringUtils.isNotBlank(jobConfigBody)){
                //上传配置文件 为了减少其他组件的使用，暂时把信息保存到数据库
                /*String configName=String.format(Constants.DATAX_JOB_NAME_PREFIX,keyId,Constants.FILE_EXTENSION);
                InputStream inputStream= IOUtils.toInputStream(jobConfigBody);
                String fileName=fileService.uploadFile(inputStream,path,configName);
                if(!StringUtils.isNotBlank(fileName)){
                    throw new CommonException(ResultEnum.FTP_UPLOAD_FAIL);
                }
                log.info("{}上传配置文件成功,文件地址是:{}",Constants.LOG_PREFIX,fileName);*/

                //保存配置文件信息到数据库
                JobConfig jobConfig=new JobConfig();
                jobConfig.setJobId(keyId);
                jobConfig.setContent(jobConfigBody);
                jobConfigService.save(jobConfig);
                log.info("{}保存配置信息成功",Constants.LOG_PREFIX);
            }else {
                throw new CommonException(ResultEnum.JOB_CONFIG_NOT_EXIST);
            }
        }

        //保存任务信息到数据库
        JobInfo jobInfo=new JobInfo();
        BeanUtils.copyProperties(jobInfoParams,jobInfo);
        jobInfo.setId(keyId);
        jobInfo=jobInfoRepository.save(jobInfo);

        //把任务加入到quartz调度
        jobSchedulerCore.addJob(jobInfo.getId(),String.valueOf(jobInfo.getJobGroup()),jobInfo.getCron(),jobInfo.getStartAt());
        return jobInfo;
    }


    @Override
    public JobInfo findOne(String jobId) {

        JobInfo jobInfo=jobInfoRepository.findById(jobId).orElse(null);

        log.debug("{}查询单个任务信息, 结果:{}", Constants.LOG_PREFIX, jobInfo);

        return jobInfo;
    }


    public void pauseJob(String jobId,String groupId){

        jobSchedulerCore.pauseJob(jobId,groupId);
    }

    public void resumeJob(String jobId,String groupId){

        jobSchedulerCore.resumeJob(jobId,groupId);
    }


    public void deleteJob(String jobId,String groupId){

        jobSchedulerCore.removeJob(jobId,groupId);

        //删除数据库
        jobInfoRepository.deleteById(jobId);
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
