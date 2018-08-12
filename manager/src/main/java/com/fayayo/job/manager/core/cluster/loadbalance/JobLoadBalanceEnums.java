package com.fayayo.job.manager.core.cluster.loadbalance;

import com.fayayo.job.common.enums.CodeEnum;
import lombok.Getter;

/**
 * @author dalizu on 2018/8/12.
 * @version v1.0
 * @desc 任务执行的策略
 */
@Getter
public enum JobLoadBalanceEnums implements CodeEnum{

    HASH(1,"hash"),//hash
    RANDOM(2,"随机"),//随机
    ROUNDROBIN(3,"轮训"),//轮训
    WEIGHT(4,"权重"),//权重

    ;

    JobLoadBalanceEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;

    private String desc;




}
