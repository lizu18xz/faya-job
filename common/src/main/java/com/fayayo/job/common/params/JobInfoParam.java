package com.fayayo.job.common.params;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class JobInfoParam {
    private Integer id;

    private Integer jobGroup;//执行器id

    private String cron;

    private String jobDesc;

    private String jobType;

    private Integer jobLoadBalance;

    private Integer jobHa;

    private String jobGroupName;//执行器名称

    private Date startAt;

    //@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private Date createTime;

    private Date updateTime;
}
