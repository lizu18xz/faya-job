package com.fayayo.job.core.transport;

import com.fayayo.job.core.transport.bean.*;
import com.fayayo.job.core.transport.spi.MessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 客户端处理类
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<DefaultResponse> {

    private MessageHandler messageHandler;

    public NettyClientHandler(MessageHandler messageHandler){
        this.messageHandler=messageHandler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultResponse response) throws Exception {

        messageHandler.handle(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("NettyChannelHandler channelActive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelActive();
    }

}
