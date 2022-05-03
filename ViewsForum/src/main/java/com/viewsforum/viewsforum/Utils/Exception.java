package com.viewsforum.viewsforum.Utils;

public enum Exception {

    INVALID_PARAM("参数格式非法"),
    UNAUTHORIZED("无权访问"),
    NOT_FOUND("未找到对象"),
    FORWARD_TIMEOUT("请求转发超时"),
    INTERNAL_ERROR("操作失败");

    private final String message;

    Exception(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}