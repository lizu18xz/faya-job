package com.fayayo.job.core.transport.codec;

import com.fayayo.job.core.transport.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc 无论我们是在客户端还是服务端，当我们收到数据之后，首先要做的事情就是把二进制数据转换到我们的一个 Java 对象
 */
public class PacketDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}
