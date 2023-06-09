package com.web.billim.common.exception;

import com.web.billim.common.handler.ErrorCode;

public class DuplicateEmailException extends BusinessException {

	public DuplicateEmailException() {
		super(ErrorCode.DUPLICATE_EMAIL);
	}

}
