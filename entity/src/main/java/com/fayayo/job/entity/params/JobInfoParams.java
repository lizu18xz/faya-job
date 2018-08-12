package com.fayayo.job.entity.params;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

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



    @NotNull(message = "任务的jobGroup不能为空")
    private Integer jobGroup;

    @NotNull(message = "执行周期不能为空")
    private String cron;

    @NotNull(message = "任务的描述不能为空")
    private String jobDesc;

    @NotNull(message = "执行类型不能为空")
    private String jobType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startAt;

    private Integer jobLoadBalance=3;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
