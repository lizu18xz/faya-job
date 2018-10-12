package com.fayayo.job.core;

import com.fayayo.job.core.service.ExecutorRun;
import com.fayayo.job.core.transport.client.NettyClient;
import com.fayayo.job.core.transport.future.ResponseFuture;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.core.transport.protocol.response.ResponsePacket;
import com.fayayo.job.core.transport.util.RequestIdGenerator;

/**
 * @author dalizu on 2018/10/11.
 * @version v1.0
 * @desc
 */
public class TransportTest {

    //模拟调用
    public static void main(String[] args) throws Exception {
        try {
            for (int i=0;i<1;i++){
                NettyClient client=new NettyClient("127.0.0.1",8888);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean flag=false;
                        try {
                            flag=client.open();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(flag){
                            RequestPacket request=new RequestPacket();
                            request = new RequestPacket();
                            request.setRequestId(RequestIdGenerator.getRequestId());
                            request.setInterfaceName(ExecutorRun.class.getName());
                            request.setMethodName("run");
                            request.setParamtersDesc("void");
                            ResponsePacket response=client.request(request);// 会阻塞 直到服务端返回后调用onSuccess回调
                            System.out.println("start  get result");
                            System.out.println("result:"+response.getValue());//获取执行结果
                        }
                    }
                }).start();
                //eventLoopGroup.shutdownGracefully();
            }
            //client.close();
        }catch (Exception e){
            e.printStackTrace();
            //client.close();
        }finally {
            //eventLoopGroup.shutdownGracefully();
        }
    }


}
