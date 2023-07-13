package com.web.billim.common.exception.handler;

import com.web.billim.common.exception.BusinessException;
import com.web.billim.common.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
		log.error(ex.getMessage(), ex);
		return ErrorResponse.toResponseEntity(ex.getErrorCode());
	}

	@ExceptionHandler(value = {JwtException.class})
	public ResponseEntity<ErrorResponse> handlerException(JwtException e){
		return ErrorResponse.toResponseEntity(e.
				getErrorCode());
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException ex) {
		StringBuilder message = new StringBuilder("잘못된 사용자 입력이 있습니다.");
		ex.getConstraintViolations().forEach(error -> {
			message.append(" - ").append(error.getMessage());
		});
		return ErrorResponse.toResponseEntity(ErrorCode.INVALIDATION_INPUT, message.toString());
	}

	@ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
	public ResponseEntity<ErrorResponse> handleValidationException(BindException ex) {
		StringBuilder message = new StringBuilder("잘못된 사용자 입력이 있습니다.");
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			message.append(" - ").append(error.getDefaultMessage());
		});
		return ErrorResponse.toResponseEntity(ErrorCode.INVALIDATION_INPUT, message.toString());
	}

}
