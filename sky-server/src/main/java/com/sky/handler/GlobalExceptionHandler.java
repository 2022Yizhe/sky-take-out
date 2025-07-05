package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex 表示捕获 BaseException (?) 异常
     * @return REST 响应结果
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex){
        String message = ex.getMessage();
        log.error("[Handler] Runtime 异常：{}", ex.getMessage());

        return Result.error(message);
    }

    /**
     * 捕获 SQL 异常
     * @param ex 表示捕获 SQLIntegrityConstraintViolation (Duplicate entry) 异常
     * @return REST 响应结果
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        log.error("[Handler] SQL 异常：{}", message);

        // Duplicate entry 'lisi' for key 'employee.idx_username'
        if (message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String msg = split[2] + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

    /**
     * 捕获修改密码异常
     * @param ex 表示捕获 PasswordEditFailedException 异常
     * @return REST 响应结果
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(PasswordEditFailedException ex){
        String message = ex.getMessage();
        log.error("[Handler] 修改密码异常：{}", message);

        return Result.error(message);
    }


}
