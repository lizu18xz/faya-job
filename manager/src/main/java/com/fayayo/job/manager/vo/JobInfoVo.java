package com.fayayo.job.manager.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author dalizu on 2018/10/30.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class JobInfoVo {

    private String id;

    private Integer jobGroup;

    private String cron;

    private String jobDesc;

    private String executorType;

    private String jobType;

    private Integer jobLoadBalance;

    private Integer jobHa;

    private Integer retries;//重试次数

    private Integer jobStatus;

    private Date startAt;

    private Date createTime;

    private Date updateTime;

    private String jobContent;

}
