package com.fayayo.job.manager.core.cluster;


import com.fayayo.job.core.extension.Scope;
import com.fayayo.job.core.extension.Spi;
import com.fayayo.job.core.transport.spi.Request;
import com.fayayo.job.core.transport.spi.Response;
 /**
   *@描述 SPI机制 每次创建新对象
 */
@Spi(scope = Scope.PROTOTYPE)
public interface HaStrategy {

    Response doCall(Request request, LoadBalance loadBalance);

}
