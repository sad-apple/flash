package com.flash.web.response;

/**
 * 返回枚举类，所有枚举类需实现此接口
 *
 * @author wuxb
 */
public interface ResponseEnumService {

    /**
     * get response enum code
     *
     * @return int
     */
    int code();

    /**
     * get response enum message
     *
     * @return String
     */
    String message();

}
