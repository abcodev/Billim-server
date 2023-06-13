package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenExpiredException extends RuntimeException {
    private ErrorCode errorCode;
}
