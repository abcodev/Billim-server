package com.web.billim.exception;

import com.web.billim.exception.handler.ErrorCode;

public class NotFoundException extends BusinessException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
