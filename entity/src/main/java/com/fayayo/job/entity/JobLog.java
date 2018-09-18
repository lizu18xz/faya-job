package com.fayayo.job.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author dalizu on 2018/9/18.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
@Entity
@Table(name = "faya_job_log")
public class JobLog {

    @Id
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

}
