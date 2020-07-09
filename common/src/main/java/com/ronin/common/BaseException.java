package com.ronin.common;

import org.springframework.http.HttpStatus;

/**
 * 自定义异常类的基类
 *
 * @author 应颂浩
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 6774128001166822675L;

    private ErrorResult errorResult;

    public BaseException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public BaseException(ErrorResult er) {
        super(er.getMessage());
        this.errorResult = er;
    }

    protected abstract HttpStatus httpStatus();

    public final ErrorResult getErrorResult() {
        return errorResult;
    }
}
