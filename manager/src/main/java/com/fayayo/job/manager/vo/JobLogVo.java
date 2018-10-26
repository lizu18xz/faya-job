package com.fayayo.job.manager.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author dalizu on 2018/10/26.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class JobLogVo {

    private String id;

    private String jobId;

    private String jobDesc;

    private String remoteIp;

    private String loadBalance;

    private String ha;

    private Integer status;

    private Integer retry;

    private String message;

    private Date createTime;

    private Date updateTime;

    private String executorType;

}
