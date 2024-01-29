package com.suchtool.router4j.server.common.advice;

import com.suchtool.router4j.server.common.entity.ResultWrapper;
import com.suchtool.router4j.server.common.exception.BusinessException;
import com.suchtool.router4j.server.common.exception.SystemException;
import com.suchtool.router4j.server.common.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResultWrapper<?> handleException(Exception e) throws Exception {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.failure().message(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResultWrapper<?> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.failure().message(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public ResultWrapper<?> handleSystemException(SystemException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return ResultWrapper.failure().message(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResultWrapper<?> handleNullPointerException(NullPointerException e) {
        log.error(e.getMessage(), e);

        // 如果某个自定义异常有@ResponseStatus注解，就继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        String message = ThrowableUtil.getLastStackTrace(e, null);
        return ResultWrapper.failure().message(message);
    }
}