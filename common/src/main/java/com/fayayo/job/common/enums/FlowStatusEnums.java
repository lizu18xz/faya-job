package com.fayayo.job.common.enums;

import lombok.Getter;

/**
 * @author dalizu on 2018/9/4.
 * @version v1.0
 * @desc 任务状态
 */
@Getter
public enum FlowStatusEnums {
    ON_LINE(1,"上线"),
    OFF_LINE(2,"下线"),
    ;

    FlowStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;


}
