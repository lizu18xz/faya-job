package com.fayayo.job.entity.enums;

import lombok.Getter;

/**
 * @author dalizu on 2018/9/4.
 * @version v1.0
 * @desc 任务状态
 */
@Getter
public enum  JobStatusEnums {

    WAIT(0,"待分配"),
    WAIT_SCHEDULER(1,"待调度"),
    ON_LINE(2,"上线"),
    OFF_LINE(3,"下线"),
    ;

    JobStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;


}
