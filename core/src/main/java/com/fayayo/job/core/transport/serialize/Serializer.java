package com.fayayo.job.core.transport.serialize;

import com.fayayo.job.core.transport.serialize.impl.JSONSerializer;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc 序列化接口
 */
public interface Serializer {

    Serializer DEFAULT=new JSONSerializer();


    /**
     * java 对象转换成二进制
     */
    byte[] serializer(Object object);


    /**
     * 二进制转为java对象
     */
    <T> T deserialize(Class<T> clazz,byte[]bytes);


    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlogrithm();


}
