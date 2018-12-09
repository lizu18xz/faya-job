package com.fayayo.job.common.params;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dalizu on 2018/8/22.
 * @version v1.0
 * @desc 传到具体执行器的参数
 */
@Getter
@Setter
public class JobInfoParam implements Serializable {

    private String id;

    private String logId;//没执行一次任务对应一次日志记录,用于唯一的标志

    private String jobFlow;   //任务流id

    private Integer jobGroup;//执行器id

    private String cron;

    private String jobDesc;

    private String jobType;

    private String executorType;

    private Integer jobLoadBalance;

    private Integer jobHa;

    private Integer retries;//重试次数

    private String jobGroupName;//执行器名称

    private Integer jobStatus;

    private String jobConfig;

    //@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
