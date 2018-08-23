package com.fayayo.job.core.transport;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.core.transport.bean.*;
import com.fayayo.job.core.transport.thread.TransportThreadPool;
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
public class NettyServerHandler extends SimpleChannelInboundHandler<DefaultRequest> {

    /**
     * @描述 线程池处理任务
     */
    private static TransportThreadPool threadPool = new TransportThreadPool();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultRequest request) throws Exception {
        log.info("{}receive:{}", Constants.LOG_PREFIX,request.getRequestId());
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DefaultResponse response = new DefaultResponse();
                response.setRequestId(request.getRequestId());
                response.setValue("ok.....");
                ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        });
    }

}
