package com.web.billim.exception;

import com.web.billim.exception.handler.ErrorCode;

public class BadRequestException extends BusinessException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
