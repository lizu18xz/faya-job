package com.fayayo.job.core.executor.bean;

import lombok.Data;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc  任务统一返回
 */
@Data
public class Result <T>{

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;

    public Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static Result success(Object data){

        return new Result(0,data);
    }



}
