package com.fayayo.job.common.enums;

import lombok.Getter;

/**
 * @author dalizu on 2018/9/4.
 * @version v1.0
 * @desc 任务状态
 */
@Getter
public enum JobStatusEnums {
    ON_LINE(1,"上线"),
    OFF_LINE(2,"下线"),
    WAIT(3,"待分配"),
    WAIT_SCHEDULER(4,"待调度"),
    RUNING(5,"正在执行"),
    ;

    JobStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;


}
