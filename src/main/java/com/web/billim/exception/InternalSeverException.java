package com.web.billim.exception;

import com.web.billim.exception.handler.ErrorCode;

public class InternalSeverException extends BusinessException{
    public InternalSeverException(ErrorCode errorCode) {
        super(errorCode);
    }
    public InternalSeverException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
