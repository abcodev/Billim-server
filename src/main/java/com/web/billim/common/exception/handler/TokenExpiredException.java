package com.web.billim.common.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenExpiredException extends RuntimeException {
    private ErrorCode errorCode;
}
