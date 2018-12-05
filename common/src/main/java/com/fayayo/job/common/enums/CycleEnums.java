package com.fayayo.job.common.enums;

import lombok.Getter;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc 调度方式 单次任务,分钟任务,小时任务,天任务,周任务，月任务
 */
@Getter
public enum CycleEnums {

    ONE(1,"单次任务"),
    MINUTE(2,"分钟任务"),
    HOUR(3,"小时任务"),
    DAY(4,"天任务"),
    WEEK(5,"周任务"),
    MON(6,"月任务"),
    ;

    private Integer name;

    private String message;

    CycleEnums(Integer name, String message) {
        this.name = name;
        this.message = message;
    }
}
