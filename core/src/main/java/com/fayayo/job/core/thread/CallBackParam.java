package com.fayayo.job.core.thread;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Future;

/**
 * @author dalizu on 2018/9/5.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class CallBackParam {

    private Integer jobId;

    private Future<?> future;

    public CallBackParam(Integer jobId, Future<?> future) {
        this.jobId = jobId;
        this.future = future;
    }

}
