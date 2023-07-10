package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final ErrorCode errorCode;

    public JwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
