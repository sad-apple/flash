package com.ndsc.biz.config;

import com.ndsc.core.exception.BizException;
import com.ndsc.core.web.Response;
import com.ndsc.core.web.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zsp
 * @date 2023/4/4 17:15
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<?> handleJeecgBootException(BizException e){
        log.error(e.getMessage(), e);
        return Response.failure(e.getMessage());
    }

}
