package com.fayayo.job.common.enums;

import lombok.Getter;

/**
 * @author dalizu on 2018/9/13.
 * @version v1.0
 * @desc
 */
@Getter
public enum  JobExecutorTypeEnums {

    DATAX("DATAX","DATAX执行器")
            ;

    private String name;

    private String message;

    JobExecutorTypeEnums(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
