package com.flash.web.handler;

import com.flash.framework.exception.BaseException;
import com.flash.web.response.Response;
import com.flash.web.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.flash.web.response.ResponseEnum.SYSTEM_ERROR;

/**
 * 自定义异常处理器
 *
 * @author panda
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<?> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Response.failure("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> notFount(RuntimeException e) {
        log.error("运行时异常:", e);
        return Response.error("运行时异常:" + e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Response.error("服务器错误，请联系管理员");
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Response> handleBaseException(BaseException e) {
        return Response.failure(e.getMessage());
    }
}
