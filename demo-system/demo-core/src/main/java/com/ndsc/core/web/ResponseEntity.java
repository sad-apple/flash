package com.ndsc.core.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 返回包装类，遵循httpcode或遵循ResponseEnum.java枚举类
 *
 * @param <T>
 * @author wuxb
 */
@ApiModel("返回对象")
public class ResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    @ApiModelProperty("返回编码，200为成功，其余为失败")
    private Integer code;
    /**
     * 返回内容
     */
    @ApiModelProperty("具体对象")
    private T data;
    /**
     * 返回说明
     */
    @ApiModelProperty("返回说明")
    private String message;

    public ResponseEntity() {
        this.code = ResponseEnum.SUCCESS.code();
    }

    public ResponseEntity(T data) {
        this.code = ResponseEnum.SUCCESS.code();
        this.data = data;
    }

    public ResponseEntity(ResponseEnumService service) {
        this.code = service.code();
        this.message = service.message();
    }

    public ResponseEntity(ResponseEnumService service, T data) {
        this.code = service.code();
        this.message = service.message();
        this.data = data;
    }

    public ResponseEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseEntity(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
