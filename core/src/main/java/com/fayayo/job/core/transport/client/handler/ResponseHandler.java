package com.fayayo.job.core.transport.client.handler;

import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 客户端处理服务端的返回
 */
@Slf4j
public class ResponseHandler extends SimpleChannelInboundHandler<ResponsePacket> {

    private MessageHandler messageHandler;

    public ResponseHandler(MessageHandler messageHandler){
        this.messageHandler=messageHandler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponsePacket response) throws Exception {

        messageHandler.handle(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ResponseHandler channelActive: remote={} local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelActive();
    }

}
