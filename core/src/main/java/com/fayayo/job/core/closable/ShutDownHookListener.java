package com.fayayo.job.core.closable;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * @author dalizu on 2018/8/16.
 * @version v1.0
 * @desc 为了关闭在tomcat中运行的server（运行tomcat的shutdown.sh关闭而不是手动kill pid
 */
@Slf4j
public class ShutDownHookListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
      log.info("ShutDownHookListener listen......");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ShutDownHook.runHook(true);
    }
}
