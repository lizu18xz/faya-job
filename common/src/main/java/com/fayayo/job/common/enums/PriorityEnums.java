package com.fayayo.job.common.enums;

import lombok.Getter;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc 任务流的优先级
 */
@Getter
public enum PriorityEnums implements CodeEnum{

    HIGH_PRIORITY(1,"高"),
    MEDIUM_PRIORITY(2,"中"),
    LOW_PRIORITY(3,"低"),
    ;

    private Integer code;

    private String message;

    PriorityEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
