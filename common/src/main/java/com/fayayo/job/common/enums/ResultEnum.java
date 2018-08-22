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


    /**
     * 调度
     * */
    CREATE_SCHEDULE_ERROR(20001,"创建调度任务失败!!!"),
    JOB_REPEAT_ERROR(20002,"不能重复添加相同的调度任务!!!"),
    JOB_NOT_EXIST(20003,"任务不存在了!!!"),
    JOB_NOT_FIND_ADDRESS(20004,"任务获取不到执行地址!!!"),


    //ha
    HA_NOT_EXIST(30001,"任务ha策略配置不存在，请检查!!!"),


    //transport
    NETTY_SEND_ERROR(40001,"NettyChannel send request to server Error")


    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
