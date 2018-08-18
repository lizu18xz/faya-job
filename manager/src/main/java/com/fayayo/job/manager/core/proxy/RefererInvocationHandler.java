
package com.fayayo.job.manager.core.proxy;
import com.fayayo.job.manager.core.cluster.Endpoint;
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

    private Endpoint endpoint;

    public RefererInvocationHandler(Class<T> clz,Endpoint endpoint) {
        this.clz = clz;
        this.endpoint=endpoint;
    }

     /**
       *@描述 具体实现请求的方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        log.info("RefererInvocationHandler invock start......");
        log.info("serverAddress:{}",endpoint.getHost());
        log.info("setCreateMillisTime:{}",System.currentTimeMillis());
        log.info("setClassName:{}",method.getDeclaringClass().getName());
        log.info("setMethodName:{}",method.getName());
        log.info("setParameterTypes:{}",method.getParameterTypes());
        log.info("setParameters:{}",args);

        return null;
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
