package com.web.billim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.security.domain.LoginReq;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/auth/login","POST"));
        this.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;

    }
    private final ObjectMapper objectMapper;
    // JSON 에서 JAVA 객체, JAVA 객체에서 JSON 으로 변환해줌
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        InputStream requestBody = request.getInputStream();
        LoginReq loginReq = new ObjectMapper().readValue(requestBody, LoginReq.class);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.getEmail(),loginReq.getPassword());
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
