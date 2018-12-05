package com.fayayo.job.core.transport.codec;

import com.fayayo.job.core.transport.protocol.Packet;
import com.fayayo.job.core.transport.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc 编码 对象转二进制  Packet抽象基类
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {

        try {
            PacketCodeC.INSTANCE.encode(out, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
