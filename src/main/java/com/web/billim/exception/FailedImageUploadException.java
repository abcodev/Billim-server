package com.web.billim.exception;

import com.web.billim.exception.handler.BusinessException;
import com.web.billim.exception.handler.ErrorCode;

public class FailedImageUploadException extends BusinessException {

	public FailedImageUploadException(Throwable cause) {
		super(ErrorCode.IMAGE_UPLOAD_FAILED, cause);
	}

}
