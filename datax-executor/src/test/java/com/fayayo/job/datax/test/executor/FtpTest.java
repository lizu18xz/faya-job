package com.fayayo.job.datax.test.executor;

import com.fayayo.job.common.util.FtpUtil;
import com.fayayo.job.datax.config.FtpProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author dalizu on 2018/9/13.
 * @version v1.0
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FtpTest {

    @Autowired
    private FtpProperties ftpProperties;

    @Test
    public void test(){

        FtpUtil ftpUtil=new FtpUtil(ftpProperties.getIp(),ftpProperties.getUsername(),ftpProperties.getPassword());


        ftpUtil.downLoadFtpFile(ftpProperties.getServerPath(),"baf4fd11-c630-46a7-a997-013f55f74c60.json","D:\\Test\\job.json");




    }

}
