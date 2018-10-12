
package com.fayayo.job.core.transport.future;

import com.fayayo.job.core.transport.protocol.response.ResponsePacket;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 响应对象异步处理
 */
public interface ResponseFuture extends Future {


    void onSuccess(ResponsePacket response);

    void onFailure(ResponsePacket response) ;


    long getRequestId();

    int getTimeout();

}
