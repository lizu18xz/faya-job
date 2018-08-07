package com.fayayo.job.core.rpc;

import com.fayayo.job.core.rpc.bean.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcResponse rpcResponse;

    public RpcResponse getRpcResponse() {
        return rpcResponse;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
        this.rpcResponse=rpcResponse;
    }


}
