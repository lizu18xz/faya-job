package com.fayayo.job.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "faya_job_info")
public class JobInfo {

    @Id
    private Integer id;

    private Integer jobGroup;

    private String cron;

    private String jobDesc;

    private String jobType;

    private Date startAt;

    private Date endAt;

    //@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
