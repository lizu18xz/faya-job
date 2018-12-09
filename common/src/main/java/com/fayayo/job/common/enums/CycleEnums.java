package com.fayayo.job.common.enums;

import lombok.Getter;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc 调度方式 单次任务,分钟任务,小时任务,天任务,周任务，月任务
 */
@Getter
public enum CycleEnums implements CodeEnum{

    ONE(1,"单次"),
    MINUTE(2,"分钟"),
    HOUR(3,"小时"),
    DAY(4,"天"),
    WEEK(5,"周"),
    MON(6,"月"),
    ;

    private Integer code;

    private String message;

    CycleEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
