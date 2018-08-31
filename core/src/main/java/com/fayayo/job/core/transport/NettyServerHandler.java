package com.fayayo.job.core.transport;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.transport.bean.*;
import com.fayayo.job.core.transport.thread.TransportThreadPool;
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
 * @desc netty 服务端处理类
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<DefaultRequest> {
    /**
     * @描述 线程池处理任务
     */
    private static TransportThreadPool threadPool = new TransportThreadPool();

    private Map<String, Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap){
        this.serviceMap=serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultRequest request) throws Exception {
        log.info("{}receive:{}", Constants.LOG_PREFIX,request.toString());
        threadPool.execute(new Runnable() {
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
                    DefaultResponse response = new DefaultResponse();
                    response.setRequestId(request.getRequestId());
                    response.setValue(result);
                    ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    log.error("处理请求信息异常:{}",e);
                }
            }
        });
    }

}
