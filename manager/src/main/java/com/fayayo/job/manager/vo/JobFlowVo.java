package com.fayayo.job.manager.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author dalizu on 2018/12/5.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class JobFlowVo {

    private String id;

    private String name;

    private String flowDesc;

    private Integer seq;

    private Integer jobCycle;

    private String jobCycleDesc;

    private Integer jobCycleValue;

    private Integer flowPriority;

    private String flowPriorityDesc;

    private Integer flowStatus;

    private Date startAt;

    private String startAtStr;

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
