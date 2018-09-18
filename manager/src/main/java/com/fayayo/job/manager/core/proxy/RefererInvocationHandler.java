
package com.fayayo.job.manager.core.proxy;
import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.util.ReflectUtil;
import com.fayayo.job.core.transport.bean.DefaultRequest;
import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
import com.fayayo.job.core.transport.util.RequestIdGenerator;
import com.fayayo.job.manager.core.cluster.support.Cluster;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 动态代理核心类
 */
@Slf4j
public class RefererInvocationHandler<T> implements InvocationHandler {

    protected Class<T> clz;

    private Cluster cluster;

    public RefererInvocationHandler(Class<T> clz, Cluster cluster) {
        this.clz = clz;
        this.cluster=cluster;
    }

     /**
       *@描述 代理方法会去发送请求到服务端,服务端获取到参数，进行方法的调用
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("{}RefererInvocationHandler invock start......", Constants.LOG_PREFIX);
        DefaultRequest request=new DefaultRequest();
        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setArguments(args);
        String methodName = method.getName();
        request.setMethodName(methodName);
        request.setParamtersDesc(ReflectUtil.getMethodParamDesc(method));
        request.setParamtersTypes(method.getParameterTypes());
        request.setInterfaceName(method.getDeclaringClass().getName());
        Response response=cluster.call(request);//发送请求
        return response.getValue();//接收请求
    }

    /**
     * tostring,equals,hashCode,finalize等接口未声明的方法不进行远程调用
     *
     * @param method
     * @return
     */
    public boolean isLocalMethod(Method method) {
        if (method.getDeclaringClass().equals(Object.class)) {
            try {
                Method interfaceMethod = clz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                return false;
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }


}
