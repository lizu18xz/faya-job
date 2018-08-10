package com.fayayo.job.core.transport;

import com.fayayo.job.core.transport.bean.RpcRequest;
import com.fayayo.job.core.transport.bean.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 服务端处理类
 */
@Slf4j
public class NettyServerHandler  extends SimpleChannelInboundHandler<RpcRequest> {

    private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {

        log.info("receive:{}",request.getServerAddress());
        //TODO 处理请求的地方  优化  改为异步处理
        RpcResponse rpcResponse=new RpcResponse();
        rpcResponse.setResult("ok");
        ctx.channel().writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);

    }

}
