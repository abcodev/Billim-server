package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

import javax.security.sasl.AuthenticationException;

@Getter
@AllArgsConstructor
public class TokenExpiredException extends RuntimeException {
    private ErrorCode errorCode;
}
