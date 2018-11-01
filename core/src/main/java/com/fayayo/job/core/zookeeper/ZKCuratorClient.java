package com.fayayo.job.core.zookeeper;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.closable.Closable;
import com.fayayo.job.core.closable.ShutDownHook;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
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
public class ZKCuratorClient implements Closable {

    private CuratorFramework client = null;

    @Autowired
    private ZkProperties zkProperties;

    public void init() {

        if (client != null) {
            return;
        }

        //启动zk客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        client = CuratorFrameworkFactory.builder().connectString(zkProperties.getZookeeperServer())
                .sessionTimeoutMs(10000).connectionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("admin").build();

        client.start();

        try {
            // 判断在admin命名空间下是否有jobRegister节点  /job-register   后续注册操作在此下面
            if (client.checkExists().forPath(zkProperties.getRegisterPath()) == null) {
                /**
                 * 对于zk来讲，有两种类型的节点:
                 * 持久节点: 当你创建一个节点的时候，这个节点就永远存在，除非你手动删除
                 * 临时节点: 你创建一个节点之后，会话断开，会自动删除，当然也可以手动删除
                 */
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)        // 节点类型：持久节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)            // acl：匿名权限
                        .forPath(zkProperties.getRegisterPath());
            }
            log.info("zookeeper服务器状态：{}", client.getState());
            addChildWatch(zkProperties.getRegisterPath());
            ShutDownHook.registerShutdownHook(this);//加入到hook事件
        } catch (Exception e) {
            log.error("zookeeper客户端连接、初始化错误...");
            e.printStackTrace();
        }
    }


    private PathChildrenCache pcache=null;


    /**
     * @描述 监听事件
     * 永久监听指定节点下的节点,只能监听指定节点下一级节点的变化,可以监听到的事件：节点创建、节点数据的变化、节点删除等
     */
    private void addChildWatch(String registerPath) throws Exception {
        pcache = new PathChildrenCache(client, registerPath, true);
        pcache.start();
        pcache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {

                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {

                    String path = event.getData().getPath();
                    log.info("{}当前系统存在的执行器:{}", Constants.LOG_PREFIX, path);

                    addChildsWatch(path);

                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {

                }

            }
        });
    }

    private PathChildrenCache cache=null;

    private void addChildsWatch(String registerPath) throws Exception {
        cache = new PathChildrenCache(client, registerPath, true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {

                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {

                    String path = event.getData().getPath();
                    String data = new String(event.getData().getData());
                    log.info("{}执行器:{},有新服务加入:{}", Constants.LOG_PREFIX, registerPath.substring(registerPath.lastIndexOf(Constants.JOIN_SYMBOL)+1,
                            registerPath.length()), data);

                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    log.info("{}执行器:{},有服务退出", Constants.LOG_PREFIX, registerPath);
                }
            }
        });
    }

    /**
     * @描述 创建持久节点
     */
    public void createPersistentNode(String path) {

        try {
            if (client.checkExists().forPath(path) == null) {
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)        // 节点类型：持久节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)            // acl：匿名权限
                        .forPath(path);
                log.info("{}create createPersistentNode:{}",  Constants.LOG_PREFIX,path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("zookeeper创建持久节点失败...{}", path);
        }

    }


    /**
     * @描述 创建临时顺序节点(和持久节点不同的是 ， 临时节点的生命周期和客户端会话绑定 。 也就是说 ， 如果客户端会话失效 ， 那么这个节点就会自动被清除掉)
     */
    public void createPhemeralEphemeralNode(String path, String address) {

        try {
            if (client.checkExists().forPath(path) == null) {
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)        // 临时顺序节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)            // acl：匿名权限
                        .forPath(path, address.getBytes());
                log.info("{}create createePhemeralEphemeralNode:{}", Constants.LOG_PREFIX,path);

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("zookeeper创建临时顺序节点失败...{}", path);
        }

    }


    public List<String> getChildNode(String path) {

        try {
            if (client.checkExists().forPath(path) == null) {
                log.info("serviceNode is not exists:{}", path);
                return null;
            }

            List<String> list = client.getChildren().forPath(path);//获取所有子节点
            if (CollectionUtils.isEmpty(list)) {
                log.info("can not find any address node on path:{}", path);
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ResultEnum.GET_CHILDNODE_ERROR);
        }
    }


    /**
     * @描述 获取节点下面的值  ip地址
     */
    public String getData(String path) {

        String dataPath = path;
        try {
            byte[] result = client.getData().forPath(dataPath);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @描述 关闭zk连接
     */
    @Override
    public void closeResource() {
        if (client != null) {
            log.info("释放zk客户端的连接......{}",client.getState());
            if(pcache!=null){
                CloseableUtils.closeQuietly(pcache);
            }
            if(cache!=null){
                CloseableUtils.closeQuietly(cache);
            }

            if(client!=null){
                CloseableUtils.closeQuietly(client);
            }
            log.info("释放zk客户端的连接result......{}",client.getState());
        }
    }


}
