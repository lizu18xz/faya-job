package com.fayayo.job.core.executor.result;

import com.fayayo.job.common.util.JsonMapper;
import lombok.Data;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author dalizu on 2018/8/31.
 * @version v1.0
 * @desc  任务统一返回
 */
@Data
public class Result <T> implements Serializable,Writable {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;

    public Result() {
        super();
    }

    public Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static Result success(Object data){

        return new Result(0,data);
    }

    public static Result error(String msg){

        return new Result(-1,msg);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        String jsonStr = JsonMapper.obj2String(this);
        WritableUtils.writeString(dataOutput, jsonStr);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        String jsonStr = WritableUtils.readString(dataInput);
        Result<T> event = JsonMapper.string2Obj(jsonStr, new TypeReference<Result<T>>() { });
        BeanUtils.copyProperties(event, this);
    }
}
