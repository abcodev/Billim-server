package com.web.billim.security.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.billim.common.exception.handler.ErrorCode;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.common.exception.AuthenticationBusinessException;
import com.web.billim.common.exception.handler.ErrorResponse;

@Component
public class AuthenticationFailureEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		System.out.println("commence() called");
		ObjectMapper objectMapper = new ObjectMapper();
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServletResponse.getWriter().write(Objects.requireNonNull(objectMapper.writeValueAsString(ErrorCode.EXPIRED_TOKEN)));
	}


//		Object exception = request.getAttribute("exception");
//
//		if(exception instanceof ErrorCode){
//			ErrorCode errorCode = (ErrorCode) exception;
//			setResponse(response,errorCode);
//			return;
//		}
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");


	private void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().print(ErrorResponse.toResponseEntity(code));
	}

}



//		if (failed instanceof AuthenticationBusinessException) {
//			AuthenticationBusinessException ex = (AuthenticationBusinessException) failed;
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//			try (OutputStream outputStream = response.getOutputStream()) {
//				new ObjectMapper().writeValue(outputStream, ErrorResponse.toResponseEntity(ex.getErrorCode()).getBody());
//				outputStream.flush();
//			}
//		} else {
//			new SimpleUrlAuthenticationFailureHandler().onAuthenticationFailure(request, response, failed);
//		}