package com.fayayo.job.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc 任务执行器
 */
@Getter
@Setter
@Entity
@Table(name = "faya_job_group")
public class JobGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String groupDesc;

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
