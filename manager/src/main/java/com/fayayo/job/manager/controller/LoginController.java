package com.fayayo.job.manager.controller;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import com.fayayo.job.entity.User;
import com.fayayo.job.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dalizu on 2018/9/7.
 * @version v1.0
 * @desc
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResultVO<?>login(@RequestParam("username") String username,
                            @RequestParam("password")String password){

        User user=userService.checkUserAndPassword(username,password);
        if(user==null){
            return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
        }
        return ResultVOUtil.success(user);
    }

}
