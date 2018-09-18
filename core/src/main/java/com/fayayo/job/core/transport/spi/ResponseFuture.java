
package com.fayayo.job.core.transport.spi;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 响应对象异步处理
 */
public interface ResponseFuture extends Future, Response {


    void onSuccess(Response response);

    void onFailure(Response response) ;
    
    long getCreateTime();

    void setReturnType(Class<?> clazz);


}
