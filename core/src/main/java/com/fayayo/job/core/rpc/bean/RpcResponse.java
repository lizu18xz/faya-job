package com.fayayo.job.core.rpc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc rpc返回的封装类
 */
@Getter
@Setter
public class RpcResponse {

    private String error;
    private Object result;

}
