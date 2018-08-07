package com.fayayo.job.core.rpc;

import com.fayayo.job.core.rpc.bean.RpcRequest;
import com.fayayo.job.core.rpc.bean.RpcResponse;
import com.fayayo.job.core.rpc.codec.RpcDecoder;
import com.fayayo.job.core.rpc.codec.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class NettyServer {

    //服务启动
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //EventLoopGroup businessGroup = new NioEventLoopGroup(1000);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //outBound(处理写)  encoder
                    ch.pipeline().addLast(new RpcEncoder(RpcResponse.class));
                    //inBound (处理读)  decoder  -->send
                    ch.pipeline().addLast(new RpcDecoder(RpcRequest.class));
                    ch.pipeline().addLast(new NettyServerHandler());

                }
            });
            //bootstrap.bind(8888).addListener((ChannelFutureListener) future -> System.out.println("bind success in port: " + 8888));
            ChannelFuture f = bootstrap.bind(8888).sync().
                    addListener((ChannelFutureListener) future -> System.out.println("bind success in port: " + 8888));//服务端启动的入口
            f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
