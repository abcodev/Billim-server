package com.web.billim.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.jwt.JwtTokenRedisService;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.security.dto.LoginRequest;
import com.web.billim.security.dto.LoginResponse;
import com.web.billim.security.dto.LoginAuthenticationToken;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.jwt.JwtUtils;
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
import java.io.OutputStream;

@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final JwtTokenRedisService jwtTokenRedisService;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils, JwtTokenRedisService jwtTokenRedisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.jwtTokenRedisService = jwtTokenRedisService;
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
        String accessToken  = jwtUtils.createAccessToken(memberId,memberGrade);
        String refreshToken = jwtUtils.createRefreshToken();
        RedisJwt redisJwt = new RedisJwt(1,refreshToken);
        jwtTokenRedisService.saveToken(redisJwt);

        LoginResponse token = new LoginResponse(accessToken,refreshToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonToken);
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        if (failed.getCause() instanceof UnAuthorizedException) {
//            UnAuthorizedException unAuthorizedException = (UnAuthorizedException) failed.getCause();
//            ErrorResponse errorResponse = ErrorResponse.toResponseEntity(unAuthorizedException.getErrorCode()).getBody();
//            response.setStatus(errorResponse.getCode().equals(HttpStatus.UNAUTHORIZED.name()) ? HttpStatus.UNAUTHORIZED.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            ObjectMapper objectMapper = new ObjectMapper();
//            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//        } else {
//            super.unsuccessfulAuthentication(request, response, failed);
//        }
//    }

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

