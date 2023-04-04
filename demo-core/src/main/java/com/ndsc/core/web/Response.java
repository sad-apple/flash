package com.ndsc.core.web;

/**
 * 接口返回包装工具类
 *
 * @author wuxb
 */
public final class Response {

    private Response() {
    }

    public static <T> ResponseEntity<T> success() {
        return response(ResponseEnum.SUCCESS.code(), ResponseEnum.SUCCESS.message());
    }

    public static <T> ResponseEntity<T> success(final T t) {
        return response(ResponseEnum.SUCCESS.code(), t, ResponseEnum.SUCCESS.message());
    }

    public static <T> ResponseEntity<T> failure() {
        return response(ResponseEnum.FAILURE.code(), ResponseEnum.FAILURE.message());
    }

    public static <T> ResponseEntity<T> failure(String message) {
        return response(ResponseEnum.FAILURE.code(), message);
    }

    public static <T> ResponseEntity<T> response(final ResponseEnumService e) {
        return response(e.code(), e.message());
    }

    public static <T> ResponseEntity<T> response(final T data, final ResponseEnumService e) {
        return response(e.code(), data, e.message());
    }

    private static <T> ResponseEntity<T> response(final int code, final String msg) {
        return response(code, null, msg);
    }

    private static <T> ResponseEntity<T> response(final int code, final T data, final String msg) {
        return new ResponseEntity<>(code, data, msg);
    }

}
