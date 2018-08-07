package com.fayayo.job.core.rpc;

import com.fayayo.job.core.rpc.bean.RpcRequest;
import com.fayayo.job.core.rpc.bean.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler  extends SimpleChannelInboundHandler<RpcRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {

        System.out.println("receive:"+request.getServerAddress());
        //TODO 处理请求的地方  优化  改为异步处理
        RpcResponse rpcResponse=new RpcResponse();
        rpcResponse.setResult("ok");
        ctx.channel().writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);

    }

}
