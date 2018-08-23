
package com.fayayo.job.core.transport.exception;

import lombok.Getter;

@Getter
public class NettyServiceException extends RuntimeException{
    private static final long serialVersionUID = -3491276058323309898L;

    protected String errorMsg = null;

    private Integer code;

    public NettyServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public NettyServiceException(String message) {
        super(message);
        this.errorMsg = message;
    }
}
