package com.fayayo.job.core.transport.bean;

import com.fayayo.job.core.transport.spi.Response;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 响应对象
 */
@Getter
@Setter
public class DefaultResponse implements Response, Serializable {
    private static final long serialVersionUID = 4281186647291615871L;

    private Object value;
    private Exception exception;
    private long requestId;
    private long processTime;
    private int timeout;

    public DefaultResponse() {}

    public DefaultResponse(long requestId) {
        this.requestId = requestId;
    }

    public DefaultResponse(Response response) {
        this.value = response.getValue();
        this.exception = response.getException();
        this.requestId = response.getRequestId();
        this.processTime = response.getProcessTime();
        this.timeout = response.getTimeout();
    }

    public DefaultResponse(Object value) {
        this.value = value;
    }

    public DefaultResponse(Object value, long requestId) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }


}
