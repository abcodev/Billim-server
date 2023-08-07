package com.web.billim.exception;

import com.web.billim.exception.handler.ErrorCode;

public class ForbiddenException extends BusinessException{
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
