package com.fayayo.job.core.zookeeper.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dalizu on 2018/12/21.
 * @version v1.0
 * @desc 服务地址的缓存
 */
@Slf4j
public class ServerCache {

    private static final Map<String,List<String>> cache=new ConcurrentHashMap<>();

     /**
       *@描述 新增服务地址到缓存
     */
    public static void addCache(String key,String value){

        log.info("addCache:key:{},val:{}",key,value);
        List<String>list=getAllServer(key);
        list.add(value);
        cache.put(key,list);
    }


    /**
     *@描述 从缓存获取执行器对应的服务地址列表
     */
    public static List<String> getAllServer(String key){
        log.info("getAllServer:key:{}",key);
        List<String>list=cache.get(key);
        if(CollectionUtils.isEmpty(list)){
            //查询,加入到缓存
            list=new ArrayList<>();
        }
        return list;
    }


    /**
     *@描述 更新在缓存中的执行器服务地址列表
     */
    public static void removeCache(String key,String value){
        log.info("removeCache:key:{},val:{}",key,value);
        List<String>list=getAllServer(key);
        list.remove(value);
        cache.put(key,list);
    }


}
