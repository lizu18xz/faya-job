package com.fayayo.job.core.transport.protocol;

import com.fayayo.job.core.transport.protocol.command.Command;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import com.fayayo.job.core.transport.serialize.Serializer;
import com.fayayo.job.core.transport.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc 工具类  按照定义的协议封装成二进制的过程
 */
public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;//魔术

    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;

    private PacketCodeC(){

        packetTypeMap = new HashMap<Byte, Class<? extends Packet>>();
        packetTypeMap.put(Command.RPC_REQUEST, RequestPacket.class);
        packetTypeMap.put(Command.RPC_RESPONSE, ResponsePacket.class);

        serializerMap = new HashMap<Byte, Serializer>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlogrithm(), serializer);

    }


    public void encode(ByteBuf byteBuf, Packet packet){

        // 1. 创建 ByteBuf 对象
        // 2. 序列化 Java 对象
        byte[]bytes=Serializer.DEFAULT.serializer(packet);
        // 3. 实际编码过程  魔数,版本,序列化算法,指令,数据长度,数据
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

    }


    public Packet decode(ByteBuf byteBuf){

        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        //读取具体的数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        //转换为对象
        Class<? extends Packet> requestType =getRequestType(command);
        if (requestType==null){
            throw new RuntimeException("没有配置参数类型");
        }

        Serializer serializer=getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }


    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }


}
