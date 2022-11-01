package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return  返回前端的结果
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")) {
            return R.error(exception.getMessage().split(" ")[2]+"已存在");
        }
        return R.error("未知错误");
    }

    /**
     * 异常处理方法
     * @return  返回前端的结果
     */
    @ExceptionHandler({CustomException.class})
    public R<String> exceptionHandler(CustomException exception) {
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
