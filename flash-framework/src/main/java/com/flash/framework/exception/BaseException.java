package com.flash.framework.exception;


/**
 * @author zsp
 * @date 2023/5/30 11:34
 */
public class BaseException extends RuntimeException{

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
