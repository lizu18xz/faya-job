package com.fayayo.job.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author dalizu on 2018/9/14.
 * @version v1.0
 * @desc 任务所需要的配置文件内容
 */
@Getter
@Setter
@Entity
@Table(name = "faya_job_config")
public class JobConfig {

    @Id
    private String jobId;

    private String content;

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
