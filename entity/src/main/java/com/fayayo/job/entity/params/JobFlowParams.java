package com.fayayo.job.entity.params;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author dalizu on 2018/11/3.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class JobFlowParams {

    private String id;

    @NotNull(message = "任务流名称不能为空")
    private String name;

    private String flowDesc;

    private Integer seq;

    @NotNull(message = "任务流执行周期类型不能为空")
    private Integer jobCycle;

    @NotNull(message = "任务流执行周期值不能为空")
    private Integer jobCycleValue;

    private Integer flowPriority;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startAt;


}
