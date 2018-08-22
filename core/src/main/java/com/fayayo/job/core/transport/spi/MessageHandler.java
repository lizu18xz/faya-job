
package com.fayayo.job.core.transport.spi;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 处理message
 */
public interface MessageHandler {

    Object handle(Object message);

}
