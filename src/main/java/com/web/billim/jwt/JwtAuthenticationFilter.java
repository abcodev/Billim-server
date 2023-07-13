package com.web.billim.jwt;

import com.web.billim.common.exception.JwtException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.jwt.dto.JwtAuthenticationToken;
import com.web.billim.security.SecurityFilterSkipMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtUtils jwtUtils;

    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;

    public JwtAuthenticationFilter(JwtUtils jwtUtils,SecurityFilterSkipMatcher securityFilterSkipMatcher) {
        this.jwtUtils = jwtUtils;
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = resolveToken(request, AUTHORIZATION_HEADER);
            if (jwtUtils.tokenValidation(jwt)) {
                JwtAuthenticationToken jwtAuthenticationToken = jwtUtils.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e){
            throw  new JwtException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    private String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
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