package com.fayayo.job.core.transport.protocol.command;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc 消息指令类型
 */
public interface Command {

    //发送具体消息
    Byte RPC_REQUEST = 1;

    Byte RPC_RESPONSE = 2;

}
