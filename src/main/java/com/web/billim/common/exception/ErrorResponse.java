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

}
