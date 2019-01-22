package com.fayayo.job.core.rpc;

import com.fayayo.job.core.closable.Closable;
import com.fayayo.job.core.extension.Scope;
import com.fayayo.job.core.extension.Spi;

import java.util.concurrent.CountDownLatch;

/**
 * @author dalizu on 2019/1/22.
 * @version v1.0
 * @desc 每次创建新对象
 */
@Spi(scope = Scope.PROTOTYPE)
public interface RpcServer extends Closable{

    void init(Integer port, String server, String logPath);

    void start(CountDownLatch countDownLatch);

}
