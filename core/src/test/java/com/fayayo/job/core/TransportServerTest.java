package com.fayayo.job.core;

import com.fayayo.job.core.transport.server.NettyServer;

import java.util.concurrent.CountDownLatch;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc
 */
public class TransportServerTest {

    //服务启动
    public static void main(String[] args) {
        try {
            NettyServer nettyServer=new NettyServer("127.0.0.1",8888,"ccc");
            nettyServer.start(new CountDownLatch(1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
