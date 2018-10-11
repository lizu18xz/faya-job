package com.fayayo.job.core.transport.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
@Sharable
public class ConnectionCountHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger nConnection = new AtomicInteger();

    //每两秒统计一次连接数
    public ConnectionCountHandler() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("connections: " + nConnection.get());
        }, 0, 2, TimeUnit.SECONDS);
    }

     /**

       *@描述 服务发现

       *@参数  服务名称
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        nConnection.incrementAndGet();
        Channel channel = ctx.channel();
        if (nConnection.get() >= 10) {
            // 超过最大连接数限制，直接close连接
            log.warn("NettyServerChannelManage channelConnected channel size out of limit: limit={} current={}", 10, nConnection.get());
            channel.close();
        } else {
            nConnection.incrementAndGet();
        }
    }

    //连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        nConnection.decrementAndGet();
    }

}


