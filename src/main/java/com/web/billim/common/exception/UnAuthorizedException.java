package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public class UnAuthorizedException extends BusinessException{
    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
