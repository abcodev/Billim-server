package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public class OrderFailedException extends BusinessException{

    public OrderFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

}
