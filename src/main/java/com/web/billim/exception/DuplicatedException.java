package com.web.billim.exception;

import com.web.billim.exception.handler.BusinessException;
import com.web.billim.exception.handler.ErrorCode;

public class DuplicatedException extends BusinessException {

	public DuplicatedException(ErrorCode errorCode) {
		super(errorCode);
	}

}
