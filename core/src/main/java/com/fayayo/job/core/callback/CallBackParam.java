package com.fayayo.job.core.callback;

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

    private String jobId;

    private Future<?> future;

    public CallBackParam(String jobId, Future<?> future) {
        this.jobId = jobId;
        this.future = future;
    }

}
