package com.fayayo.job.common.exception;

import com.fayayo.job.common.enums.ResultEnum;
import lombok.Getter;

/**
 * @author dalizu on 2018/8/5.
 * @version v1.0
 * @desc
 */
@Getter
public class CommonException extends RuntimeException{

    private Integer code;

    public CommonException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }


}
