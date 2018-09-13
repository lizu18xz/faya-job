package com.fayayo.job.manager.controller;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import com.fayayo.job.core.service.impl.ZkServiceDiscovery;
import com.fayayo.job.core.service.impl.ZkServiceRegistry;
import com.fayayo.job.core.zookeeper.ZKCuratorClient;
import com.fayayo.job.core.zookeeper.ZkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dalizu on 2018/8/5.
 * @version v1.0
 * @desc
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ZKCuratorClient zkCuratorClient;

    @Autowired
    private ZkProperties zkProperties;

    @GetMapping("/register/{ip}")
    public void register(@PathVariable("ip") String ip){

        ZkServiceRegistry zkServiceRegistry=new ZkServiceRegistry(zkCuratorClient,zkProperties);

        zkServiceRegistry.register("1",ip);

    }



    @GetMapping("/discover")
    public ResultVO<String> test(){


        ZkServiceDiscovery zkServiceDiscovery=new ZkServiceDiscovery(zkCuratorClient,zkProperties);

        List<String> list=zkServiceDiscovery.discover("testService");

        if(list==null){
            throw new CommonException(ResultEnum.DISCOVER_SERVICE_NULL);
        }

        for (String s:list){

            System.out.println("address:{}"+zkServiceDiscovery.getData(s));

        }

        return ResultVOUtil.success();
    }

}
