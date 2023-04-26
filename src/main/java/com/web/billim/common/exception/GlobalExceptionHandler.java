package com.web.billim.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException ex) {
		StringBuilder message = new StringBuilder("잘못된 사용자 입력이 있습니다.\n");
		ex.getConstraintViolations().forEach(error -> {
			message.append(" - ").append(error.getPropertyPath()).append(" 은(는) ").append(error.getMessage()).append("\n");
		});
		return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", message.toString()));
	}

	@ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
	public ResponseEntity<ErrorResponse> handleValidationException(BindException ex) {
		StringBuilder message = new StringBuilder("잘못된 사용자 입력이 있습니다.\n");
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			message.append(" - ").append(error.getField()).append(" 은(는) ").append(error.getDefaultMessage()).append("\n");
		});
		return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", message.toString()));
	}

}
