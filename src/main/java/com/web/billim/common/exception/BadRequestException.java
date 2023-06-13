package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public class BadRequestException extends BusinessException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
