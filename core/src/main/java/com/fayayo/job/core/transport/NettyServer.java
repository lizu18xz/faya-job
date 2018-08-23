package com.fayayo.job.core.transport;

import com.fayayo.job.core.transport.bean.*;
import com.fayayo.job.core.transport.codec.RpcDecoder;
import com.fayayo.job.core.transport.codec.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 服务端类
 */
@Slf4j
public class NettyServer {

    private Integer port;

    public NettyServer(Integer port) {
        this.port = port;
    }


     /**
       *@描述 启动服务端
     */
    public void start(){
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
                    ch.pipeline().addLast(new NettyServerHandler());
                }
            });
            //同步
            ChannelFuture f = bootstrap.bind(port).sync().
                    addListener((ChannelFutureListener) future -> System.out.println("bind success in port: " + port));//服务端启动的入口
            f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    //服务启动
    public static void main(String[] args) {
        NettyServer nettyServer=new NettyServer(8888);
        nettyServer.start();
    }
}
