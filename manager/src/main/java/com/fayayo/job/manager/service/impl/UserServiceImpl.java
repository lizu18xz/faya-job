package com.fayayo.job.manager.service.impl;

import com.fayayo.job.entity.User;
import com.fayayo.job.manager.repository.UserRepository;
import com.fayayo.job.manager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dalizu on 2018/9/7.
 * @version v1.0
 * @desc
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUserAndPassword(String username, String password) {
        User user=userRepository.findByUsernameAndPassword(username,password);
        if(user==null){
            return null;
        }
        return user;
    }
}
