package com.fayayo.job.core.rpc.hadoop;

import com.fayayo.job.core.extension.SpiMeta;
import com.fayayo.job.core.rpc.RpcServer;
import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.core.service.impl.ExecutorRunImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author dalizu on 2019/1/22.
 * @version v1.0
 * @desc rpc rpc 服务端
 */
@SpiMeta(name = "hadoopServer")
@Slf4j
public class HadoopServer implements RpcServer {

    private Integer port;

    private String host;//当前执行器的地址

    private String logPath;

    private RPC.Server server = null;

    @Override
    public void init(Integer port, String server, String logPath) {
        this.port = port;
        this.host = server;
        this.logPath=logPath;
    }

    public void start(CountDownLatch countDownLatch) {

        // 创建一个RPC builder
        RPC.Builder builder = new RPC.Builder(new Configuration());

        //指定RPC Server的参数
        builder.setBindAddress(host);
        builder.setPort(port);

        builder.setProtocol(ExecutorRun.class);
        builder.setInstance(new ExecutorRunImpl(new StringBuilder().
                append(host).append(":").
                append(port).toString(), logPath));

        //创建Server
        try {
            server= builder.build();
            //启动服务
            server.start();
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("hadoop rpc start error!!!");
        }
    }


    @Override
    public void closeResource() {
        if(server!=null){
            server.stop();
        }
    }
}
