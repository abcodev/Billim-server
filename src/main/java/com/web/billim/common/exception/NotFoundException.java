package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public class NotFoundException extends BusinessException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
