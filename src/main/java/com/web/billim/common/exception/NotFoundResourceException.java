package com.web.billim.common.exception;

import com.web.billim.common.handler.ErrorCode;

public class NotFoundResourceException extends BusinessException {

	public NotFoundResourceException(Class<?> type) {
		super(ErrorCode.RESOURCE_NOT_FOUND, String.format("%s 정보를 찾을 수 없습니다.", type.getName()));
	}

}
