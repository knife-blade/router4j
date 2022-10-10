package com.knife.router4j.server.common.entity;

import com.knife.router4j.server.common.constant.ResultCode;
import lombok.Data;

@Data
public class ResultWrapper<T> {
    private boolean success;

    private Integer code;

    private T data;

    private String message;

    private ResultWrapper() {
    }

    public static <T> ResultWrapper<T> success() {
        return assemble(ResultCode.SUCCESS.getCode(), true);
    }

    public static <T> ResultWrapper<T> failure() {
        return assemble(ResultCode.SYSTEM_FAILURE.getCode(), false);
    }

    public ResultWrapper<T> data(T data) {
        this.setData(data);
        return this;
    }

    public ResultWrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultWrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    private static <T> ResultWrapper<T> assemble(int code, boolean success) {
        ResultWrapper<T> resultWrapper = new ResultWrapper<>();
        resultWrapper.setCode(code);
        resultWrapper.setSuccess(success);

        return resultWrapper;
    }
}