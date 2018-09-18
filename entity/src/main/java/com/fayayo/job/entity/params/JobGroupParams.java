package com.fayayo.job.entity.params;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author dalizu on 2018/8/23.
 * @version v1.0
 * @desc 任务执行器参数
 */
@Getter
@Setter
public class JobGroupParams {

    @NotNull(message = "执行器名称不能为空")
    private String name;

    @NotNull(message = "执行器描述不能为空")
    private String groupDesc;

    private Integer seq;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
