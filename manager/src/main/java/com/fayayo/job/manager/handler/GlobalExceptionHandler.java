package com.fayayo.job.manager.handler;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.common.result.ResultVO;
import com.fayayo.job.common.result.ResultVOUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dalizu on 2018/8/5.
 * @version v1.0
 * @desc 全局的异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**

     *@描述 处理项目中自定义的异常CommonException

     *@创建人  dalizu

     *@创建时间  2018/8/5

     */
    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public ResultVO handlerCommonException(CommonException e){
        e.printStackTrace();
        return ResultVOUtil.error(e.getCode(),e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO handlerCommonException(Exception e){

        e.printStackTrace();

        return ResultVOUtil.error(ResultEnum.UNKNOWN_ERROR);
    }



}
