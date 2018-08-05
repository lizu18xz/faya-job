package com.fayayo.job.core.zookeeper;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author dalizu on 2018/8/4.
 * @version v1.0
 * @desc zk的客户端连接实现
 */
@Slf4j
public class ZKCuratorClient {

    private CuratorFramework client = null;

    @Autowired
    private ZkProperties zkProperties;

    public void init() {

        if(client!=null){
            return;
        }

        //启动zk客户端
        RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,5);
        client = CuratorFrameworkFactory
                .newClient(zkProperties.getZookeeperServer(), 10000, 10000,retryPolicy);
        client.start();
        client = client.usingNamespace("admin");

        try {
            // 判断在admin命名空间下是否有jobRegister节点  /job-register   后续注册操作在此下面
            if (client.checkExists().forPath(zkProperties.getRegisterPath()) == null) {
                /**
                 * 对于zk来讲，有两种类型的节点:
                 * 持久节点: 当你创建一个节点的时候，这个节点就永远存在，除非你手动删除
                 * 临时节点: 你创建一个节点之后，会话断开，会自动删除，当然也可以手动删除
                 */
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)		// 节点类型：持久节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)			// acl：匿名权限
                        .forPath(zkProperties.getRegisterPath());
                log.info("zookeeper初始化成功...");

                log.info("zookeeper服务器状态：{}", client.getState());
            }
        } catch (Exception e) {
            log.error("zookeeper客户端连接、初始化错误...");
            e.printStackTrace();
        }
    }


    /**
     *@描述 创建持久节点
     */
    public void createPersistentNode(String path){

        try {
            if(client.checkExists().forPath(path) == null){
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)		// 节点类型：持久节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)			// acl：匿名权限
                        .forPath(path);
                log.info("create createPersistentNode:{}",path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("zookeeper创建持久节点失败...{}",path);
        }

    }



    /**
     *@描述 创建临时顺序节点(和持久节点不同的是，临时节点的生命周期和客户端会话绑定。也就是说，如果客户端会话失效，那么这个节点就会自动被清除掉)
     */
    public void createPhemeralEphemeralNode(String path,String address){

        try {
            if(client.checkExists().forPath(path) == null){
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)		// 临时顺序节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)			// acl：匿名权限
                        .forPath(path,address.getBytes());
                log.info("create createePhemeralEphemeralNode:{}",path);

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("zookeeper创建临时顺序节点失败...{}",path);
        }

    }


    public List<String> getChildNode(String path){

        try {
            if(client.checkExists().forPath(path) == null){
                log.info("serviceNode is not exists:{}",path);
                return null;
            }

            List<String> list=client.getChildren().forPath(path);//获取所有子节点
            if(CollectionUtils.isEmpty(list)){
                log.info("can not find any address node on path:{}",path);
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ResultEnum.GET_CHILDNODE_ERROR);
        }
    }


    public String getData(String path,String address){

        String dataPath=path+"/"+address;

        try {
            byte[] result=client.getData().forPath(dataPath);

            return new String(result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }












}
