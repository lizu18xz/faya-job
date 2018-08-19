package com.fayayo.job.common.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fayayo.job.common.constants.Constants;
import lombok.Data;

/**
 * http请求返回的最外层对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResultVO<T> {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;

    @JsonIgnore
    public boolean isSuccess(){
        return  this.code== Constants.CODE_SUCCESS;
    }
}
