package com.fayayo.job.entity.params;

import com.fayayo.job.common.enums.JobStatusEnums;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author dalizu on 2018/8/8.
 * @version v1.0
 * @desc 任务参数
 */
@Getter
@Setter
public class JobInfoParams {

    /*@NotNull(message = "任务的jobGroup不能为空")
    private Integer jobGroup;*/

    private String id;

    @NotNull(message = "所属任务流不能为空")
    private String jobFlow;


    @NotNull(message = "任务的描述不能为空")
    private String jobDesc;

    @NotNull(message = "任务类型不能为空")
    private String jobType;

    @NotNull(message = "执行器类型不能为空")
    private String executorType;//执行器类型


    @NotNull(message = "负载策略不能为空")
    private Integer jobLoadBalance;

    @NotNull(message = "高可用方式不能为空")
    private Integer jobHa;

    private Integer retries;

    private String jobConfig;//任务配置信息（DATAX任务才有)

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
