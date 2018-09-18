package com.fayayo.job.core.transport;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.core.service.impl.ExecutorRunImpl;
import com.fayayo.job.core.transport.bean.*;
import com.fayayo.job.core.transport.codec.RpcDecoder;
import com.fayayo.job.core.transport.codec.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 服务端类
 */
@Slf4j
public class NettyServer {

    //保存调度任务接口和实现类的映射关系
    private static Map<String, Object> serviceMap = new HashMap<String, Object>();

    private Integer port;

    private String server;//当前执行器的地址

    public NettyServer(String server,Integer port) {
        this.server=server;
        this.port = port;
        serviceMap.put(ExecutorRun.class.getName(),new ExecutorRunImpl(server));//保存调度任务接口和实现类的映射关系
    }

    private Thread thread;

     /**
       *@描述 启动服务端
     */
    public void start(CountDownLatch countDownLatch){

        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                //ConnectionCountHandler connectionCountHandler=new ConnectionCountHandler();//统计
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup);
                    bootstrap.channel(NioServerSocketChannel.class);
                    bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
                    bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //outBound(处理写)  encoder
                            ch.pipeline().addLast(new RpcEncoder(DefaultResponse.class));
                            //inBound (处理读)  decoder  -->send
                            //ch.pipeline().addLast(connectionCountHandler);//统计连接数
                            ch.pipeline().addLast(new RpcDecoder(DefaultRequest.class));
                            ch.pipeline().addLast(new NettyServerHandler(serviceMap));
                        }
                    });
                    //同步
                    ChannelFuture f = bootstrap.bind(port).sync().
                            addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    log.info("{}bind success in port: ", Constants.LOG_PREFIX,port);
                                    countDownLatch.countDown();
                                }
                            });//服务端启动的入口
                    f.channel().closeFuture().sync();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        });
        thread.setDaemon(true);//守护线程
        thread.start();

    }

    //服务启动
    public static void main(String[] args) {
        NettyServer nettyServer=new NettyServer("",8888);
        nettyServer.start(null);
    }
}
