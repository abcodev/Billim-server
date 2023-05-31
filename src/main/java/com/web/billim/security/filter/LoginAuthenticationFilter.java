package com.web.billim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.redis.service.JwtTokenRedisService;
import com.web.billim.security.domain.LoginRequest;
import com.web.billim.security.domain.UserDetailsDto;
import com.web.billim.security.domain.response.LoginResponse;
import com.web.billim.security.jwt.domain.RedisJwt;
import com.web.billim.security.jwt.provider.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        // toString() : Object 클래스에 있는 메서드 (객체를 문자열로 표현하고 싶을 때)
        // 기본적으로 Object 클래스에는 toString 이 들어있어서 어떤 객체든 toString 을 호출할 수 있음
        // Object 클래스에 있기때문에 default 메서드 만 있고 내가만든 객체에서 쓰려면 오버라이딩을 해줘야함(안하면 주소가 나옴)
        // Collection<? extends GrantedAuthority> c
        // c.toString();
        // Collection-> 여러가지 요소를 가지고 있음 List<T>, Set<T>.. 의 인터페이스 (여러개의 요소를 가지고 있음, 배열)
        // Spring Security 입장에서는 권한은 여러개가 될 수 있으니까 collection 형태로 반환했지만 우리는 하나만 쓰니까
//        String grade = authResult.getAuthorities().toString();
        GrantedAuthority grade = authResult.getAuthorities().stream().findFirst().orElseThrow();
        log.info("여기요 "+ grade.getAuthority());

        String accessToken  = jwtTokenProvider.createAccessToken(memberId, grade.getAuthority());
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
//        long memberId = jwtTokenProvider.
        RedisJwt redisJwt = new RedisJwt(1,refreshToken);
        jwtTokenRedisService.saveToken(redisJwt);

        LoginResponse token = new LoginResponse(accessToken,refreshToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToken = objectMapper.writeValueAsString(token);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonToken);
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

