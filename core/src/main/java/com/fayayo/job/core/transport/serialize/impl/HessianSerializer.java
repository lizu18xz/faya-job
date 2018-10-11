
package com.fayayo.job.core.transport.serialize.impl;


import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.fayayo.job.core.transport.serialize.Serializer;
import com.fayayo.job.core.transport.serialize.SerializerAlogrithm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hession2 序列化，要求序列化的对象实现 java.io.Serializable 接口
 *
 */
public class HessianSerializer implements Serializer {

    @Override
    public byte[] serializer(Object object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Hessian2Output out = new Hessian2Output(bos);
            out.writeObject(object);
            out.flush();
            return bos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try {
            Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
            return (T) input.readObject(clazz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public byte getSerializerAlogrithm() {
        return SerializerAlogrithm.HESSIAN;
    }
}
