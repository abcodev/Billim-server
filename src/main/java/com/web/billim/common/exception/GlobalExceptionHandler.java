package com.web.billim.common.exception;

import com.web.billim.common.handler.TokenExpiredException;
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

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException ex) {
		ErrorResponse response = ErrorResponse.from(ex);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(NotFoundResourceException ex) {
		ErrorResponse response = ErrorResponse.from(ex);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(response);
	}

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

	@ExceptionHandler(value = {TokenExpiredException.class})
	public ResponseEntity<com.web.billim.common.handler.ErrorResponse> handlerException(TokenExpiredException e){
		return com.web.billim.common.handler.ErrorResponse.toResponseEntity(e.getErrorCode());
	}

	//  NotFoundException extends
	//  UnAuthorizedException extends
	//	BadRequestException extends
	//	DuplicatedException extends
	//	TokenException extends
}
