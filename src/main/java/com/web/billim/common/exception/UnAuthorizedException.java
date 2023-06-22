package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public class UnAuthorizedException extends AuthenticationBusinessException {
	public UnAuthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}

}
