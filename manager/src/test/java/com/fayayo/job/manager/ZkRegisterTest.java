package com.fayayo.job.manager;

import com.fayayo.job.core.register.ServiceRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZkRegisterTest {


    @Autowired
    private ServiceRegistry serviceRegistry;

    @Test
    public void register() {

        //groupId=1

        serviceRegistry.register("1", "192.168.76.111");

    }


    @Test
    public void discover() {


        List<String> list = serviceRegistry.discover("testService");

        for (String s : list) {
            System.out.println(s);
        }


    }


}
