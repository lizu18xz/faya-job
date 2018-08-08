package com.fayayo.job.common.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    UNKNOWN_ERROR(-1,"系统异常"),
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数不正确"),
    NEED_LOGIN(10, "用户没有登录"),
    LOGIN_FAIL(24,"登录失败"),

    /**
     * 无权限访问
     * */
    NO_AUTH_ACCESS(400,"无权限访问,请联系管理员"),


    /**
     * zk异常
     * */
    GET_CHILDNODE_ERROR(11001,"get childNode error!!!"),
    DISCOVER_SERVICE_NULL(11002,"get service is null!!!"),


    CREATE_SCHEDULE_ERROR(20001,"创建调度任务失败!!!")


    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
