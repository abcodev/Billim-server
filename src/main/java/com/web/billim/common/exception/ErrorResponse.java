package com.web.billim.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorResponse {

	private String errorCode;
	private String message;

	public static ErrorResponse from(BusinessException ex) {
		return new ErrorResponse(ex.getErrorCode().name(), ex.getMessage());
	}

}
