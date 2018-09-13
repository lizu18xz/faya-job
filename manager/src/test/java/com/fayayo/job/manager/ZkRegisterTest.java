package com.fayayo.job.manager;

import com.fayayo.job.core.service.impl.ZkServiceDiscovery;
import com.fayayo.job.core.service.impl.ZkServiceRegistry;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
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
    private ZKCuratorClient zkCuratorClient;

    @Autowired
    private ZkProperties zkProperties;

    @Test
    public void register(){

        ZkServiceRegistry zkServiceRegistry=new ZkServiceRegistry(zkCuratorClient,zkProperties);

        //groupId=1

        zkServiceRegistry.register("1","192.168.76.111");

    }



    @Test
    public void discover(){

        ZkServiceDiscovery zkServiceDiscovery=new ZkServiceDiscovery(zkCuratorClient,zkProperties);

        List<String> list=zkServiceDiscovery.discover("testService");

        for (String s:list){
            System.out.println(s);
        }


    }










}
