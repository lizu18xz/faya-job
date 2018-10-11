package com.fayayo.job.core.transport.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc 协议抽象类
 */
@Data
public abstract class Packet {

    @JSONField(deserialize = false, serialize = false)
    private Byte version=1;//协议版本


    @JSONField(serialize = false)
    public abstract Byte getCommand();//协议指令


}
