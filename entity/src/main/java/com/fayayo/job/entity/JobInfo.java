package com.fayayo.job.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "faya_job_info")
public class JobInfo {

    @Id
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

    //@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
