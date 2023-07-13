package com.web.billim.jwt;

import com.web.billim.jwt.dto.JwtAuthenticationToken;
import com.web.billim.security.SecurityFilterSkipMatcher;
import com.web.billim.security.handler.AuthenticationFailureEntryPoint;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final AuthenticationManager authenticationManager;
	private final SecurityFilterSkipMatcher securityFilterSkipMatcher;
	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
		SecurityFilterSkipMatcher securityFilterSkipMatcher) {
		this.authenticationManager = authenticationManager;
		this.securityFilterSkipMatcher = securityFilterSkipMatcher;
	}

	// before set
	public void setAuthenticationFailureHandler(AuthenticationEntryPointFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String jwt = resolveToken(request);
		try {
			JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken)authenticationManager.authenticate(new JwtAuthenticationToken(jwt));
			if (jwtAuthenticationToken.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
			}
			filterChain.doFilter(request, response);
		} catch (AuthenticationException ex) {
			// 이렇게 하면 AbstractAuthenticationProcessingFilter 의 동작방식과 같아짐,,,
			failureHandler.onAuthenticationFailure(request, response, ex);
		}
	}

	// After
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//		String jwt = resolveToken(request);
//		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authenticationManager.authenticate(new JwtAuthenticationToken(jwt));
//		if (jwtAuthenticationToken.isAuthenticated()) {
//			SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
//		}
//		filterChain.doFilter(request, response);
//	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return securityFilterSkipMatcher.shouldSkipFilter(request);
	}

}
