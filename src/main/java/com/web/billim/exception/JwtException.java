package com.web.billim.exception;

import com.web.billim.exception.handler.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtException extends AuthenticationException {
    private final ErrorCode errorCode;

    public JwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
