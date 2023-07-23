package com.web.billim.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.exception.AuthenticationBusinessException;
import com.web.billim.exception.handler.ErrorResponse;
import com.web.billim.jwt.JwtProvider;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.security.dto.LoginRequest;
import com.web.billim.security.dto.LoginResponse;
import com.web.billim.security.dto.LoginAuthenticationToken;
import com.web.billim.jwt.dto.RedisJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    ObjectMapper objectMapper = new ObjectMapper();

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.jwtService = jwtService;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        LoginRequest loginRequest = obtainEmailPassword(request);
        LoginAuthenticationToken loginAuthenticationToken = new LoginAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        return authenticationManager.authenticate(loginAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // JWT 토큰 발급 및 응답 처리 로직
        // 예시로서는 JwtTokenProvider 클래스를 사용하여 토큰을 생성하고 응답에 포함시킵니다.
        String memberId = authResult.getPrincipal().toString();
        MemberGrade memberGrade = (MemberGrade) authResult.getAuthorities().stream().findFirst().orElseThrow();
        log.info("등급"+memberGrade);
        String accessToken  = jwtProvider.createAccessToken(String.valueOf(memberId),memberGrade);
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(memberId));
        RedisJwt redisJwt = new RedisJwt(Long.parseLong(memberId),refreshToken);
        jwtService.saveToken(redisJwt);

        LoginResponse token = new LoginResponse(Long.parseLong(memberId),accessToken,refreshToken);
        String jsonToken = objectMapper.writeValueAsString(token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (failed instanceof AuthenticationBusinessException) {
            AuthenticationBusinessException ex = (AuthenticationBusinessException)failed;
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(ex)));
        }
    }

    private LoginRequest obtainEmailPassword(HttpServletRequest request) {
        try {
            InputStream requestBody = request.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(requestBody, LoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

