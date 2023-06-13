package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public class DuplicatedException extends BusinessException {

	public DuplicatedException(ErrorCode errorCode) {
		super(errorCode);
	}

}
