package com.web.billim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.redis.service.JwtTokenRedisService;
import com.web.billim.security.domain.LoginRequest;
import com.web.billim.security.domain.response.LoginResponse;
import com.web.billim.security.jwt.domain.RedisJwt;
import com.web.billim.security.jwt.provider.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
        LoginRequest loginRequest = obtainEmailPassword(request);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
//        BillimAuthentication billimAuthentication = new BillimAuthentication(loginReq.getEmail(),loginReq.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // JWT 토큰 발급 및 응답 처리 로직
        // 예시로서는 JwtTokenProvider 클래스를 사용하여 토큰을 생성하고 응답에 포함시킵니다.
        String memberId = authResult.getPrincipal().toString();
        String accessToken  = jwtTokenProvider.createAccessToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
//        long memberId = jwtTokenProvider.
        RedisJwt redisJwt = new RedisJwt(1,refreshToken);
        jwtTokenRedisService.saveToken(redisJwt);

        LoginResponse token = new LoginResponse(accessToken,refreshToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(token);
        response.getWriter().write(jsonToken);
    }


    private LoginRequest obtainEmailPassword(HttpServletRequest request) {
            try {
                InputStream requestBody = request.getInputStream();
                ObjectMapper objectMapper = new ObjectMapper();
                LoginRequest loginRequest = objectMapper.readValue(requestBody, LoginRequest.class);
                return loginRequest;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}

