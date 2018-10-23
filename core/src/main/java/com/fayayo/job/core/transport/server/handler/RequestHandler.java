package com.fayayo.job.core.transport.server.handler;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.thread.StandardThreadExecutor;
import com.fayayo.job.core.thread.StandardThreadManager;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc netty 服务端对客户端发送的请求进行处理
 */
@Slf4j
public class RequestHandler extends SimpleChannelInboundHandler<RequestPacket> {

    /**
     * @描述 线程池处理任务
     */
    private static StandardThreadExecutor transportThreadPool;

    private Map<String, Object> serviceMap;

    public RequestHandler(Map<String, Object> serviceMap,StandardThreadExecutor transportThreadPool){
        this.serviceMap=serviceMap;
        this.transportThreadPool=transportThreadPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPacket request) throws Exception {
        log.info("{}Receive Client Request:{}", Constants.LOG_PREFIX,request.toString());
        transportThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String methodName=request.getMethodName();
                String interfaceName=request.getInterfaceName();
                Object serviceBean=serviceMap.get(interfaceName);
                if(serviceBean==null){
                    throw new CommonException(ResultEnum.EXECUTOR_SERVICE_NOT_FOUND);
                }
                try {
                    Class <?>clazz=serviceBean.getClass();
                    Object[] parameters = request.getArguments();//参数
                    Class<?>[] parameterTypes=request.getParamtersTypes();
                    FastClass serviceFastClass = FastClass.create(clazz);
                    FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
                    Object result=serviceFastMethod.invoke(serviceBean, parameters);//调用真正的方法  Result<?>
                    ResponsePacket response = new ResponsePacket();//使用默认的响应对象返回
                    response.setRequestId(request.getRequestId());
                    response.setValue(result);
                    ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    //TODO 异常返回
                    log.error("处理请求信息异常:{}",e);
                }
            }
        });
    }

}
