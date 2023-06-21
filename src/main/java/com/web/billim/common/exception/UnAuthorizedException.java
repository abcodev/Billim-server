package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public class UnAuthorizedException extends AuthenticationBusinessException {
	public UnAuthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}

}

/*
    Spring Security 쪽에서 발생하는 인증 에러는 GlobalExceptionHandler 를 태울수가 없음
    Filter - Spring Context 에 안들어갔기 때문에..
   Spring Security 쪽에서 발생하는 인증 에러는 별도의 GlobalExceptionHandler 를 만들어줘야하는데 그게 EntryPoint.
 */