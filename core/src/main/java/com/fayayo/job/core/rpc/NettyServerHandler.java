package com.fayayo.job.core.rpc;

import com.fayayo.job.core.rpc.bean.RpcRequest;
import com.fayayo.job.core.rpc.bean.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 服务端处理类
 */
@Slf4j
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
