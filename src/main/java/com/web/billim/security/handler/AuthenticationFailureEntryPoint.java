package com.web.billim.security.handler;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if (failed instanceof AuthenticationBusinessException) {
			AuthenticationBusinessException ex = (AuthenticationBusinessException) failed;
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			try (OutputStream outputStream = response.getOutputStream()) {
				new ObjectMapper().writeValue(outputStream, ErrorResponse.toResponseEntity(ex.getErrorCode()).getBody());
				outputStream.flush();
			}
		} else {
			new SimpleUrlAuthenticationFailureHandler().onAuthenticationFailure(request, response, failed);
		}
	}
}