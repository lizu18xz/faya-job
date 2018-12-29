package com.fayayo.job.core.executor;

import com.fayayo.job.common.constants.Constants;
import com.fayayo.job.common.enums.JobExecutorTypeEnums;
import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.core.annotation.FayaService;
import com.fayayo.job.core.register.ServiceRegistry;
import com.fayayo.job.core.executor.handler.JobExecutorHandler;
import com.fayayo.job.core.executor.result.Result;
import com.fayayo.job.core.log.LoggerUtil;
import com.fayayo.job.core.service.impl.ExecutorRunImpl;
import com.fayayo.job.core.callback.CallbackThread;
import com.fayayo.job.core.transport.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc Task执行器 注册，获取服务等功能
 */
@Slf4j
public class JobExecutor implements ApplicationContextAware,CallbackThread.CallBackHandler {

    private static ApplicationContext applicationContext;

    private static ConcurrentHashMap<String, Object> service = new ConcurrentHashMap<String, Object>();

    @Autowired
    private ServiceRegistry serviceRegistry;

    private String server;

    private Integer port;

    private Integer weight;

    private String name;

    private String logPath;

    private static String mainClass;

    NettyServer nettyServer = null;

    public JobExecutor() {
    }

    public JobExecutor(String server, Integer port, Integer weight, String name, String mainClass, String logPath) {
        this.server = server;
        this.port = port;
        this.weight = weight;
        this.name = name;
        if (this.weight == null) {
            this.weight = 1;//如果不写默认全是1
        }
        this.mainClass = mainClass;
        this.logPath = logPath;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        //初始化RPC服务
        initRpcService();

        //启动服务端
        initServer();

        //初始化日志
        initLog();

        //注册服务
        initRegister();

        //启动结果处理线程
        callbackStart();

    }


    /**
     * @描述 服务发现 把RPC的服务保存起来
     */
    private void initRpcService() {
        Map<String, Object> services = this.applicationContext.getBeansWithAnnotation(FayaService.class);//根据注解获取所有的service
        if (!CollectionUtils.isEmpty(services)) {

            for (Object bean : services.values()) {
                if (bean.getClass().isAnnotationPresent(FayaService.class)) {
                    FayaService fayaService = bean.getClass().getAnnotation(FayaService.class);
                    Class<?> clazz = fayaService.value();
                    log.info("{}Registering service '{}' for RPC result [{}].", Constants.LOG_PREFIX, clazz.getName(), bean);
                    service.put(clazz.getName(), bean);//保存映射关系
                }
            }
        }
    }

    /**
     * @描述 根据配置获取具体执行的服务类
     */
    public static JobExecutorHandler getHandler() {
        Object object = service.get(mainClass);
        if (object == null) {
            throw new CommonException(ResultEnum.JOB_HANDLER_ERROR);
        }
        return (JobExecutorHandler) object;
    }

    private void initServer() {
        log.info("{}执行器初始化,server:{},port:{}", Constants.LOG_PREFIX, server, port);
        //然后启动这个服务端，准备接收请求
        CountDownLatch countDownLatch = new CountDownLatch(1);//阻塞线程
        nettyServer = new NettyServer(server, port, logPath);
        nettyServer.start(countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("{}服务端启动完毕,注册服务到注册中心", Constants.LOG_PREFIX);
    }

    private void initRegister() {
        //服务端启动成功后，注册此执行器的这个服务到zk

        String serviceAddress = new StringBuilder().
                append(server).append(":").
                append(port).append(":").
                append(weight).toString();
        serviceRegistry.register(name, serviceAddress);
        log.info("{}注册到服务中心完成", Constants.LOG_PREFIX);
    }


    private void initLog() {
        //DATAX任务日志单独处理
        if (!name.equals(JobExecutorTypeEnums.DATAX.getName())) {
            if (!"".equals(logPath)) {
                LoggerUtil.init(logPath);
            }
        }
    }

    private void callbackStart() {
        CallbackThread callbackThread=CallbackThread.getInstance();
        callbackThread.setCallBackHandler(this);
        callbackThread.start();
    }


    public void close() {
        log.info("{}start close resources......", Constants.LOG_PREFIX);
        nettyServer.close();
        CallbackThread.getInstance().toStop();
        ExecutorRunImpl.futureThread.shutdown();
        LoggerUtil.stop();
    }

    //回调
    @Override
    public void onNewMessageArrived(String jobId,Result<?> result) {
        log.info("{}Callable get jobId:{},Result:{}", Constants.LOG_PREFIX, jobId, result.getData());

    }

}
