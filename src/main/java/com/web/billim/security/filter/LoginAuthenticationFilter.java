package com.web.billim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.redis.service.JwtTokenRedisService;
import com.web.billim.security.domain.LoginReq;
import com.web.billim.security.domain.response.LoginRes;
import com.web.billim.security.jwt.domain.RefreshToken;
import com.web.billim.security.jwt.provider.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtTokenRedisService jwtTokenRedisService;



    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, JwtTokenRedisService jwtTokenRedisService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenRedisService = jwtTokenRedisService;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        LoginReq loginReq = obtainEmailPassword(request);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.getEmail(),loginReq.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // JWT 토큰 발급 및 응답 처리 로직
        // 예시로서는 JwtTokenProvider 클래스를 사용하여 토큰을 생성하고 응답에 포함시킵니다.
        String acceesToken  = jwtTokenProvider.createAccessToken(authResult);
        String refreshtoken = jwtTokenProvider.createRefreshToken(authResult);
        RefreshToken refreshToken = new RefreshToken(1,refreshtoken);
        jwtTokenRedisService.saveToken(refreshToken);

        LoginRes token = new LoginRes(acceesToken,refreshtoken);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(token);

        response.getWriter().write(jsonToken);
    }


    private LoginReq obtainEmailPassword(HttpServletRequest request) {
            try {
                InputStream requestBody = request.getInputStream();
                ObjectMapper objectMapper = new ObjectMapper();
                LoginReq loginReq = objectMapper.readValue(requestBody, LoginReq.class);
                return loginReq;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}

