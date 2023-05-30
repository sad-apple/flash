package com.flash.web.response;

/**
 * 基础返回枚举
 *
 * @author wuxb
 */
public enum ResponseEnum implements ResponseEnumService {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    FAILURE(201, "操作失败"),

    /**
     * 必要参数丢失
     */
    PARAMS_MISS(302, "参数丢失"),

    /**
     * 未授权
     */
    UNAUTH(401, "无访问权限"),

    /**
     * 无法找到相关的数据
     */
    NOT_FOUND(404, "无法获取数据"),

    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(500, "系统内部错误");

    private Integer code;
    private String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
