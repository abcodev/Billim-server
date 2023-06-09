package com.web.billim.common.exception;

import com.web.billim.common.handler.ErrorCode;

public class FailedImageUploadException extends BusinessException {

	public FailedImageUploadException(Throwable cause) {
		super(ErrorCode.IMAGE_UPLOAD_FAILED, cause);
	}

}
