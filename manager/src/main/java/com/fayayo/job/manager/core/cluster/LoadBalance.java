package com.fayayo.job.manager.core.cluster;



import com.fayayo.job.core.extension.Scope;
import com.fayayo.job.core.extension.Spi;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;

import java.util.List;
/**
 *@描述 SPI机制 每次创建新对象
 */
@Spi(scope = Scope.PROTOTYPE)
public interface LoadBalance {

    Endpoint select(RequestPacket request);

    void onRefresh(List<Endpoint> endpoints);

    void selectToHolder(RequestPacket request,List<Endpoint> endpointHolder);
}
