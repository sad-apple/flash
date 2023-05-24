package com.ndsc.core.exception;

/**
 * @author zsp
 * @date 2023/4/4 17:16
 */
public class BizException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public BizException(String message){
        super(message);
    }

    public BizException(Throwable cause)
    {
        super(cause);
    }

    public BizException(String message,Throwable cause)
    {
        super(message,cause);
    }
}
