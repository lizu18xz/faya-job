package com.fayayo.job.manager.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author dalizu on 2018/10/30.
 * @version v1.0
 * @desc
 */
@Getter
@Setter
public class JobGroupVo {

    private Integer id;

    private String name;

    private String groupDesc;

    private Integer seq;

    private Date createTime;

    private Date updateTime;

    private List<String> serverList;

}
