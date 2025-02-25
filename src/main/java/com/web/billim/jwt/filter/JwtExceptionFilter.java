package com.web.billim.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.exception.JwtException;
import com.web.billim.security.config.SecurityFilterSkipMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;

    public JwtExceptionFilter(SecurityFilterSkipMatcher securityFilterSkipMatcher) {
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e){
            errorResponse(e,response);
        }
    }

    public void errorResponse(JwtException e,HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(e.getErrorCode().getHttpStatus().value());
        Map<String, Object> body = new HashMap<>();
        body.put("error", e.getErrorCode());
        body.put("message", e.getMessage());
        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper 는 Bean 으로 등록해서도 많이 쓴다.
        mapper.writeValue(response.getOutputStream(), body);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return securityFilterSkipMatcher.shouldSkipFilter(request);
    }

}