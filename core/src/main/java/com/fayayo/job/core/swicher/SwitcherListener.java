package com.fayayo.job.core.swicher;

/**
 * @author dalizu on 2018/9/3.
 * @version v1.0
 * @desc 监听接口
 */
public interface SwitcherListener {

    void onValueChanged(String key, Boolean value);
}
