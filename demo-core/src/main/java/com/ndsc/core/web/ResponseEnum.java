package com.ndsc.core.web;

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
     * 未登录
     */
    UNLOGIN(401, "未登录"),

    /**
     * 未授权
     */
    UNAUTHORIZED(403, "无访问权限"),

    /**
     * 无法找到相关的数据
     */
    NOT_FOUND(404, "无法获取数据"),

    /**
     * 账号被踢
     */
    DISCONNECT_ERROR(407, "对不起，您的账号在其他地方登录，您已经被强制下线!"),

    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(500, "系统内部错误"),

    /**
     * 组织无权限
     */
    DEPT_ERROR(999, "当前账号暂无权限，请切换组织用户后重试"),

    /**
     * 数据重复效验
     */
    DUPLICATE_DATA(202, ""),

    /**
     * 重复数据统一返回值
     */
    PUSH_DATA(203, "该文书无需推送"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(601, "文件上传失败"),

    /**
     * 多人操作提示
     */
    MUTI_OPERATOR(203, "提交失败，请核对是否因多人同时操作该记录导致冲突");

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
