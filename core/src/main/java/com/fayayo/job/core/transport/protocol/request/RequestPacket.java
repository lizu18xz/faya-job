package com.fayayo.job.core.transport.protocol.request;

import com.fayayo.job.core.transport.protocol.Packet;
import com.fayayo.job.core.transport.protocol.command.Command;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc RPC请求数据包
 */
@Getter
@Setter
public class RequestPacket extends Packet implements Serializable {

    private String interfaceName;//接口名称
    private String methodName;//方法名称
    private String paramtersDesc;//参数描述

    private Class<?>[] paramtersTypes;

    private Object[] arguments;//参数
    private Map<String, String> attachments;
    private int retries = 0;

    private long requestId;//请求id


    @Override
    public String toString() {
        return interfaceName + "." + methodName + "(" + paramtersDesc + ") requestId=" + requestId;
    }

    @Override
    public Byte getCommand() {
        return Command.RPC_REQUEST;
    }
}
