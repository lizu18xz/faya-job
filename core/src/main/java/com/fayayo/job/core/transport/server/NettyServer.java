package com.fayayo.job.core.transport.server;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.core.service.impl.ExecutorRunImpl;
import com.fayayo.job.core.thread.StandardThreadExecutor;
import com.fayayo.job.core.thread.StandardThreadManager;
import com.fayayo.job.core.transport.codec.Spliter;
import com.fayayo.job.core.transport.server.handler.RequestHandler;
import com.fayayo.job.core.transport.codec.PacketDecoder;
import com.fayayo.job.core.transport.codec.PacketEncoder;
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

    private StandardThreadExecutor standardThreadExecutor = null;

    public NettyServer(String server, Integer port) {
        this.server = server;
        this.port = port;
        serviceMap.put(ExecutorRun.class.getName(), new ExecutorRunImpl(new StringBuilder().
                append(server).append(":").
                append(port).toString()));//保存调度任务接口和实现类的映射关系
    }

    EventLoopGroup bossGroup = null;
    EventLoopGroup workerGroup=null;

    /**
     * @描述 启动服务端
     */
    public void start(CountDownLatch countDownLatch) {

         bossGroup = new NioEventLoopGroup();
         workerGroup = new NioEventLoopGroup();
        //ConnectionCountHandler connectionCountHandler=new ConnectionCountHandler();//统计
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            standardThreadExecutor= StandardThreadManager.transportThreadPool();

            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast(new Spliter());

                    //in
                    ch.pipeline().addLast(new PacketDecoder());

                    //ch.pipeline().addLast(connectionCountHandler);//统计连接数
                    ch.pipeline().addLast(new RequestHandler(serviceMap,standardThreadExecutor));

                    //out
                    ch.pipeline().addLast(new PacketEncoder());

                }
            });
            //异步
            ChannelFuture f = bootstrap.bind(port).
                    addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            log.info("{}Server bind success in port: {}", Constants.LOG_PREFIX, port);
                            countDownLatch.countDown();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }


    //关闭资源
    public void close(){

        try {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            standardThreadExecutor.shutdownNow();
            log.info("NettyServer close Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
