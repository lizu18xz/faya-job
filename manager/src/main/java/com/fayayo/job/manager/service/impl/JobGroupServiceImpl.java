package com.fayayo.job.manager.service.impl;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.register.ServiceRegistry;
import com.fayayo.job.entity.JobGroup;
import com.fayayo.job.entity.JobInfo;
import com.fayayo.job.entity.params.JobGroupParams;
import com.fayayo.job.manager.repository.JobGroupRepository;
import com.fayayo.job.manager.service.JobGroupService;
import com.fayayo.job.manager.service.JobInfoService;
import com.fayayo.job.manager.vo.JobGroupVo;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class JobGroupServiceImpl implements JobGroupService{

    @Autowired
    private JobGroupRepository jobGroupRepository;

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Override
    public JobGroup saveOrUpdate(JobGroupParams jobGroupParams) {

        JobGroup jobGroup=new JobGroup();
        BeanUtils.copyProperties(jobGroupParams,jobGroup);
        jobGroup=jobGroupRepository.save(jobGroup);

        return jobGroup;
    }

    @Override
    public JobGroup findOne(Integer jobGroupId) {

        JobGroup jobGroup=jobGroupRepository.findById(jobGroupId).orElse(null);

        log.debug("{}查询单个任务执行器信息, 结果:{}", Constants.LOG_PREFIX, jobGroup);

        return jobGroup;
    }

    @Override
    public Page<JobGroupVo> query(Pageable pageable) {

        Page<JobGroup> page=jobGroupRepository.findAll(pageable);

        List<JobGroup> sortList=page.getContent();

        List <JobGroupVo>bodyList=null;
        if(!CollectionUtils.isEmpty(sortList)){

                bodyList=sortList.stream().map(e->{
                JobGroupVo jobGroupVo=new JobGroupVo();
                BeanUtils.copyProperties(e,jobGroupVo);
                List<String>list=serviceRegistry.discover(e.getName());
                if(!CollectionUtils.isEmpty(list)){
                    //获取具体的ip
                    List<String> addressList = list.stream().map(p -> {

                        return " 【"+serviceRegistry.getData(p)+"】";

                    }).collect(Collectors.toList());
                    jobGroupVo.setServerList(addressList);
                }
                return jobGroupVo;

            }).collect(Collectors.toList());

            //按照seq字段排序 seq从小到大
            Collections.sort(bodyList, new Comparator<JobGroupVo>() {
                @Override
                public int compare(JobGroupVo o1, JobGroupVo o2) {
                    return o1.getSeq() - o2.getSeq();
                }
            });
        }
        return new PageImpl<>(bodyList,pageable,page.getTotalElements());
    }

    @Override
    public JobGroup findByName(String name) {
        return jobGroupRepository.findByName(name);
    }

    @Override
    public void deleteById(Integer groupId) {


        List<JobInfo>jobInfoList=jobInfoService.findByGroupId(groupId);
        if(!CollectionUtils.isEmpty(jobInfoList)){
            throw new CommonException(ResultEnum.GROUP_HAVE_JOB);
        }

        jobGroupRepository.deleteById(groupId);
    }


}
