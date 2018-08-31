package com.fayayo.job.core.transport.bean;

import com.fayayo.job.core.transport.spi.Request;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dalizu on 2018/8/7.
 * @version v1.0
 * @desc 请求对象
 */
@Getter
@Setter
public class DefaultRequest implements Serializable, Request {

    private static final long serialVersionUID = 1168814620391610215L;

    private String interfaceName;//接口名称
    private String methodName;//方法名称
    private String paramtersDesc;//参数描述

    private Class<?>[] paramtersTypes;

    private Object[] arguments;//参数
    private Map<String, String> attachments;
    private int retries = 0;

    private long requestId;//请求id

    public void setAttachment(String key, String value) {
        if (this.attachments == null) {
            this.attachments = new HashMap<>();
        }

        this.attachments.put(key, value);
    }

    public Map<String, String> getAttachments() {
        return attachments != null ? attachments : Collections.EMPTY_MAP;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }


    @Override
    public String toString() {
        return interfaceName + "." + methodName + "(" + paramtersDesc + ") requestId=" + requestId;
    }

}
