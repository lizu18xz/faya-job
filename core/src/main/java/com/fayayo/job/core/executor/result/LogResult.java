package com.fayayo.job.core.executor.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dalizu on 2018/10/24.
 * @version v1.0
 * @desc 统一日志返回
 */
@Getter
@Setter
public class LogResult implements Serializable {

    private long pointer;

    private String content;

    public LogResult(long pointer, String content) {
        this.pointer = pointer;
        this.content = content;
    }
}
