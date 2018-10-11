package com.fayayo.job.core.transport.client;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.transport.client.handler.MessageHandler;
import com.fayayo.job.core.transport.client.handler.ResponseHandler;
import com.fayayo.job.core.transport.NettyConstants;
import com.fayayo.job.core.transport.codec.Spliter;
import com.fayayo.job.core.transport.future.*;
import com.fayayo.job.core.transport.future.impl.DefaultResponseFuture;
import com.fayayo.job.core.transport.codec.PacketDecoder;
import com.fayayo.job.core.transport.codec.PacketEncoder;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import com.fayayo.job.core.transport.util.RequestIdGenerator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 客户端类
 */
@Slf4j
public class NettyClient {

    private String remoteAddress;

    private int port;


    private static final  EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    protected ConcurrentMap<Long, ResponseFuture> callbackMap = new ConcurrentHashMap<Long, ResponseFuture>();

    private ChannelFuture channelFuture = null;
    private Channel channel = null;

    public NettyClient(String remoteAddress, int port) {
        this.remoteAddress = remoteAddress;
        this.port = port;
    }
     /**
       *@描述 连接的建立
     */
    public boolean open()throws Exception{
        //建立连接
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NettyConstants.CONNECTTIMEOUT);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {

                    ch.pipeline().addLast(new Spliter());

                    //in
                    ch.pipeline().addLast(new PacketDecoder());

                    ch.pipeline().addLast(new ResponseHandler(new MessageHandler() {
                        @Override
                        public Object handle(Object message) {
                            ResponsePacket response = (ResponsePacket) message;//获取服务的返回
                            //ResponseFuture responseFuture =callbackMap.remove(response.getRequestId());
                            ResponseFuture responseFuture =NettyClient.this.removeCallback(response.getRequestId());
                            if (responseFuture == null) {
                                log.warn("NettyClient has response from server, but responseFuture not exist, requestId={}",
                                        response.getRequestId());
                                return null;
                            }
                            if (response.getException() != null) {
                                responseFuture.onFailure(response);
                            } else {
                                responseFuture.onSuccess(response);
                            }
                            return null;
                        }
                    }));

                    //out
                    ch.pipeline().addLast(new PacketEncoder());
                }
            });

            channelFuture = bootstrap.connect(remoteAddress,port);
            int timeout = NettyConstants.CONNECTTIMEOUT;
            if (timeout <= 0) {
                throw new RuntimeException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.");
            }
            // 不去依赖于connectTimeout
            boolean result = channelFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);//阻塞直到连接建立
            boolean success = channelFuture.isSuccess();
            if (result && success) {
                channel = channelFuture.channel();//获取channel
                return true;
            }

            throw new RuntimeException("NettyClient init Error.");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     *@描述 发送请求,直接返回ResponseFuture
     */
    public ResponseFuture request(RequestPacket request){
        try {
            int timeout = NettyConstants.REQUESTTIMEOUT;
            if (timeout <= 0) {
                throw new RuntimeException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.");
            }
            //定义返回的接收值
            ResponseFuture response = new DefaultResponseFuture(request, timeout);
            registerCallback(request.getRequestId(), response);
            ChannelFuture writeFuture = this.channel.writeAndFlush(request);//发送信息到服务端
            boolean result = writeFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);//阻塞直到发送请求成功

            if (result && writeFuture.isSuccess()) {
                response.addListener(new FutureListener() {//新增监听器，当整个请求完成后会调用此回调方法
                    @Override
                    public void operationComplete(Future future) throws Exception {
                        //判断有没有异常
                        if (future.isSuccess() || (future.isDone() && future.getException() instanceof CommonException)) {
                            log.info("服务端返回数据完成.");
                        }else {
                            //TODO 可以设置异常次数然后禁用此服务
                            log.info("服务端未知异常请确认!!!");
                        }
                    }
                });
                return response;
            }

            writeFuture.cancel(true);
            response = removeCallback(request.getRequestId());
            if (response != null) {
                response.cancel();
            }
            throw new CommonException(ResultEnum.NETTY_SEND_ERROR);

        }finally {

        }
    }

    public void close(){
        eventLoopGroup.shutdownGracefully();
    }

    public void registerCallback(long requestId, ResponseFuture responseFuture) {
        if (this.callbackMap.size() >= NettyConstants.NETTY_CLIENT_MAX_REQUEST) {
            // reject request, prevent from OutOfMemoryError  请求过多
            throw new RuntimeException("NettyClient over of max concurrent request, drop request, url");
        }

        this.callbackMap.put(requestId, responseFuture);
    }

    public ResponseFuture removeCallback(long requestId) {
        return callbackMap.remove(requestId);
    }


}
