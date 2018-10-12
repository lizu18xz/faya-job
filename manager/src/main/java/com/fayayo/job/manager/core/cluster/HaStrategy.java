package com.fayayo.job.manager.core.cluster;


import com.fayayo.job.core.extension.Scope;
import com.fayayo.job.core.extension.Spi;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;

/**
   *@描述 SPI机制 每次创建新对象
 */
@Spi(scope = Scope.PROTOTYPE)
public interface HaStrategy {

    ResponsePacket doCall(RequestPacket request, LoadBalance loadBalance);

}
