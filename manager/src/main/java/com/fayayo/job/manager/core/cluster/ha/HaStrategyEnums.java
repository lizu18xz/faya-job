package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.common.enums.CodeEnum;
import lombok.Getter;

/**
 * @author dalizu on 2018/8/18.
 * @version v1.0
 * @desc Ha
 */
@Getter
public enum  HaStrategyEnums implements CodeEnum{

    FAIL_FAST(1,"快速失败"),//快速失败
    FAIL_OVER(2,"失败重试"),//失败重试
    ;

    HaStrategyEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;
}
