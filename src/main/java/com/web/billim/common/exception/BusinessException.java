package com.web.billim.common.exception;

import com.web.billim.common.exception.handler.ErrorCode;

public abstract class BusinessException extends RuntimeException {
	// RuntimeException 을 발생시키면 메세지에 의존해야하므로 상황에 맞는 에러를 발생시켜줘야함

	private final ErrorCode errorCode;

	// 생성자 Overloading
	// 상속관계에서 부모 클래스의 생성자를 호출하는 법 -> super
	// cause 라는 필드
	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
