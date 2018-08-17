
package com.fayayo.job.manager.core.proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 动态代理核心类
 */
public class RefererInvocationHandler<T> implements InvocationHandler {

    protected Class<T> clz;

    public RefererInvocationHandler(Class<T> clz) {
        this.clz = clz;
        /*init();
        interfaceName = MotanFrameworkUtil.removeAsyncSuffix(clz.getName());*/
    }

     /**
       *@描述 具体实现请求的方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //获取Ha的配置



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
