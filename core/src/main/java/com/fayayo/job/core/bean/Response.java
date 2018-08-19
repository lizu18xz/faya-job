package com.fayayo.job.core.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dalizu on 2018/8/19.
 * @version v1.0
 * @desc rcp统一返回对象
 */
@Getter
@Setter
public class Response<T> implements Serializable{

    public static int SUCCESS=200;

    public static int ERROR=-1;

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;

    private Response(Integer code) {
        this.code = code;
    }

    private Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //成功返回
    public static <T>Response<T>success(){
        return new Response<>(SUCCESS,"success");
    }

    public static <T>Response<T>success(T data){
        return new Response<>(SUCCESS,"success",data);
    }

    //失败返回
    public static <T>Response<T>error(String msg){
        return new Response<>(ERROR,msg);
    }


}
