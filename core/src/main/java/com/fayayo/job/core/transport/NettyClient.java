package com.fayayo.job.core.transport;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.transport.bean.*;
import com.fayayo.job.core.transport.codec.RpcDecoder;
import com.fayayo.job.core.transport.codec.RpcEncoder;
import com.fayayo.job.core.transport.spi.*;
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
    public void open()throws Exception{
        //建立连接
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Params.CONNECTTIMEOUT);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            //NettyClientHandler nettyClientHandler=new NettyClientHandler();
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    //outBound   (后添加的先执行  处理写)   读取消息要  decoder
                    ch.pipeline().addLast(new RpcEncoder(DefaultRequest.class));
                    //inBound   (先添加的先执行 处理读)  client 消息encoder编码 然后发送消息
                    ch.pipeline().addLast(new RpcDecoder(DefaultResponse.class));
                    ch.pipeline().addLast(new NettyClientHandler(new MessageHandler() {
                        @Override
                        public Object handle(Object message) {
                            Response response = (Response) message;
                            ResponseFuture responseFuture =NettyClient.this.removeCallback(response.getRequestId());
                            if (responseFuture == null) {
                                log.warn("NettyClient has response from server, but responseFuture not exist, requestId={}", response.getRequestId());
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
                }
            });
            channelFuture = bootstrap.connect(remoteAddress,port);
            int timeout = Params.CONNECTTIMEOUT;
            if (timeout <= 0) {
                throw new RuntimeException("NettyClient init Error: timeout(" + timeout + ") <= 0 is forbid.");
            }
            // 不去依赖于connectTimeout
            boolean result = channelFuture.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);//阻塞直到连接建立
            boolean success = channelFuture.isSuccess();
            if (result && success) {
                channel = channelFuture.channel();//获取channel
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *@描述 发送请求
     */
    public Response request(Request request){
        try {
            int timeout = Params.REQUESTTIMEOUT;
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
                        log.info("服务端返回数据完成");
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
        if (this.callbackMap.size() >= Params.NETTY_CLIENT_MAX_REQUEST) {
            // reject request, prevent from OutOfMemoryError  请求过多
            throw new RuntimeException("NettyClient over of max concurrent request, drop request, url");
        }

        this.callbackMap.put(requestId, responseFuture);
    }

    public ResponseFuture removeCallback(long requestId) {
        return callbackMap.remove(requestId);
    }

    //模拟调用
    public static void main(String[] args) throws Exception {
        NettyClient client=new NettyClient("127.0.0.1",8888);
        for (int i=0;i<1;i++){
            client.open();
            DefaultRequest request=new DefaultRequest();
            request = new DefaultRequest();
            request.setRequestId(RequestIdGenerator.getRequestId());
            request.setInterfaceName("xxxxxxxx");
            request.setMethodName("hello");
            request.setParamtersDesc("void");
            Response response=client.request(request);
            System.out.println("start  get result");
            System.out.println("result:"+response.getValue());//获取执行结果   会阻塞  知道服务端返回后调用onSuccess回调
            //eventLoopGroup.shutdownGracefully();
        }
        client.close();
    }

}
