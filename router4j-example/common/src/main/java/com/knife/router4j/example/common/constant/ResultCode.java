package com.knife.router4j.example.common.constant;

public enum ResultCode {
    SUCCESS(1000, "访问成功"),
    SYSTEM_FAILURE(1001, "系统异常"),
    ;

    private final int code;
    private final String description;

    ResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
