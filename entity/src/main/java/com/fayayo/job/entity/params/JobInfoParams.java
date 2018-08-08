package com.fayayo.job.entity.params;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class JobInfoParams {


    @NotNull(message = "任务名称不能为空")
    private String jobName;

    @NotNull(message = "任务的jobGroup不能为空")
    private String jobGroup;

    @NotNull(message = "执行周期不能为空")
    private String cron;

    private String jobDesc;

    @NotNull(message = "执行类型不能为空")
    private String jobType;

    private Date startAt;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
