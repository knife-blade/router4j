package com.knife.router4j.common.entity;

import com.knife.common.constant.ResultCode;
import lombok.Data;

@Data
public class Result<T> {
    private boolean success = true;

    private int code = ResultCode.SUCCESS.getCode();

    private String message;

    private T data;

    public Result() {
    }

    public Result(boolean success) {
        this.success = success;
    }

    public Result<T> success(boolean success) {
        Result<T> result = new Result<>(success);
        if (success) {
            result.code = ResultCode.SUCCESS.getCode();
        } else {
            result.code = ResultCode.SYSTEM_FAILURE.getCode();
        }
        return result;
    }

    public Result<T> success() {
        return success(true);
    }

    public Result<T> failure() {
        return success(false);
    }

    /**
     * @param code {@link ResultCode#getCode()}
     */
    public Result<T> code(int code) {
        this.code = code;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }
}