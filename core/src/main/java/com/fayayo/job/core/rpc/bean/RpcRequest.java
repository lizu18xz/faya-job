package com.fayayo.job.core.rpc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 请求的封装类
 */
@Getter
@Setter
public class RpcRequest {

    private String serverAddress;
    private long createMillisTime;
    private String accessToken;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

}
