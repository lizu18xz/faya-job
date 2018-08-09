package com.fayayo.job.core.rpc;

import com.fayayo.job.core.rpc.bean.RpcRequest;
import com.fayayo.job.core.rpc.bean.RpcResponse;
import com.fayayo.job.core.rpc.codec.RpcDecoder;
import com.fayayo.job.core.rpc.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 客户端类
 */
@Slf4j
public class NettyClient {
    private static final String SERVER_HOST = "127.0.0.1";
    //模拟调用
    public static void main(String[] args) throws Exception {

        NettyClient client=new NettyClient();
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setServerAddress("10.10.10.119");
        RpcResponse rpcResponse=client.send(rpcRequest);

        log.info("result:{}",rpcResponse.getResult());

    }

    public RpcResponse send(RpcRequest request)throws Exception{
        //建立连接
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            NettyClientHandler nettyClientHandler=new NettyClientHandler();

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    //outBound   (后添加的先执行  处理写)   读取消息要  decoder
                    ch.pipeline().addLast(new RpcEncoder(RpcRequest.class));
                    //inBound   (先添加的先执行 处理读)  client 消息encoder编码 然后发送消息
                    ch.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                    ch.pipeline().addLast(nettyClientHandler);
                }
            });

            ChannelFuture channelFuture=bootstrap.connect(SERVER_HOST, 8888).sync();//获取ChannelFuture

            //发送请求
            // 写入 RPC 请求数据并关闭连接
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();//同步
            return nettyClientHandler.getRpcResponse();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
