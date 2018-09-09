package com.fayayo.job.manager.service;

import com.fayayo.job.entity.User;

/**
 * @author dalizu on 2018/9/7.
 * @version v1.0
 * @desc
 */
public interface UserService {

    User checkUserAndPassword(String username, String password);

}
