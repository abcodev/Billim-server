package com.web.billim.exception;

import com.web.billim.exception.handler.ErrorCode;

public class UnAuthorizedException extends AuthenticationBusinessException {
	public UnAuthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}

