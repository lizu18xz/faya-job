
package com.fayayo.job.core.transport.spi;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 请求的封装类
 */
public interface Request {

    /**
     * 
     * service interface
     * 
     * @return
     */
    String getInterfaceName();

    /**
     * service method name
     * 
     * @return
     */
    String getMethodName();

    /**
     * service method param desc (sign)
     * 
     * @return
     */
    String getParamtersDesc();

    /**
     * service method param
     * 
     * @return
     */
    Object[] getArguments();


    /**
     * request id
     * 
     * @return
     */
    long getRequestId();

    /**
     * retries
     * 
     * @return
     */
    int getRetries();

    /**
     * set retries
     */
    void setRetries(int retries);


}
