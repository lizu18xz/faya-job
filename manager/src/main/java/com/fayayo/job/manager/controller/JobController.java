package com.fayayo.job.manager.controller;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {


    @GetMapping("/test")
    public ResultVO<String>test(){

        throw new CommonException(ResultEnum.LOGIN_FAIL);

        //return ResultVOUtil.success("www");
    }








}
