package com.web.billim.exception;

import com.web.billim.exception.handler.ErrorCode;

public class OrderFailedException extends BusinessException{
    public OrderFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
    public OrderFailedException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
