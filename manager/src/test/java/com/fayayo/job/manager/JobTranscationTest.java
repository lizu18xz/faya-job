package com.fayayo.job.manager;

import com.fayayo.job.entity.params.JobInfoParams;
import com.fayayo.job.manager.service.JobInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author dalizu on 2018/9/14.
 * @version v1.0
 * @desc 事务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobTranscationTest {


    @Autowired
    JobInfoService jobInfoService;


    /**
     * 事务测试
     * */
    @Test
    public void test(){

        JobInfoParams jobInfoParams=new JobInfoParams();
        jobInfoParams.setCron("123");
        jobInfoParams.setExecutorType("DATAX");
        jobInfoParams.setJobConfig("112121212");
        jobInfoParams.setJobDesc("dddd");
        jobInfoParams.setJobHa(1);
        //jobInfoParams.setJobGroup(1);
        jobInfoParams.setJobStatus(1);
        jobInfoParams.setJobLoadBalance(1);
        jobInfoParams.setJobType("BEAN");
        //jobInfoParams.setJobType("BEANBEANBEANBEANBEANBEANBEANBEANBEANBEAN");//error

        jobInfoService.addJob(jobInfoParams);

    }

}
