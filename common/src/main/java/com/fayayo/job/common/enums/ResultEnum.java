package com.fayayo.job.common.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    UNKNOWN_ERROR(-1,"系统异常"),
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数不正确"),
    NEED_LOGIN(10, "用户没有登录"),
    LOGIN_FAIL(24,"登录失败"),
    FTP_LOGIN_FAIL(25,"FTP登陆失败，请检查配置"),
    FTP_DOWN_FAIL(26,"FTP下载文件失败，请检查配置"),
    FTP_UPLOAD_FAIL(27,"FTP上传文件失败，请检查配置"),
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
    EXECUTOR_ADDRESS_NOT_EXIST(20004,"任务获取不到执行地址,请检查执行器是否启动!!!"),
    JOB_HANDLER_ERROR(20005,"任务执行器的引擎配置错误!!!!!!"),
    JOB_RUN_ERROR(20006,"任务执行失败，请联系管理员!!!!!!"),
    PAUSE_SCHEDULE_ERROR(20007,"暂停调度任务失败!!!"),
    RESUME_SCHEDULE_ERROR(20008,"唤醒调度任务失败!!!"),
    REMOVE_SCHEDULE_ERROR(20009,"删除调度任务失败!!!"),
    JOB_CONFIG_NOT_EXIST(20010,"DATAX任务配置内容为空,请配置!!!"),



    //ha
    HA_NOT_EXIST(30001,"任务ha策略配置不存在，请检查!!!"),


    //transport
    NETTY_SEND_ERROR(40001,"NettyChannel send request to server Error"),
    EXECUTOR_SERVICE_NOT_FOUND(40002,"调度实现类没有注册，请联系管理员!!!!"),


    //业务
    LOGIN_ERROR(50001,"登陆失败，请检查用户名密码"),
    LOG_INFO_NOT_EXIST(50002,"日志记录不存在"),
    JOB_INFO_NOT_EXIST(50003,"任务记录不存在"),
    JOB_NOT_SUPPORT_LOG(50004,"任务不支持在线查看日志"),
    GROUP_HAVE_JOB(50005,"请先删除执行器下配置的任务"),
    JOB_FLOW_HAVE_JOB(50006,"请先删除任务流下配置的任务"),
    JOB_FLOW_NOT_HAVE_JOB(50007,"请先去任务流下配置任务"),
    ERROR_DELETE_JOB(50008,"删除任务请先下线任务所属任务流"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
