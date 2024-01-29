package com.suchtool.router4j.server.common.exception;

/**
 * 系统异常
 */
public class SystemException extends RuntimeException{
    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
