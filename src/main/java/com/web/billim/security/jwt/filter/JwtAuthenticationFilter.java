package com.web.billim.security.jwt.filter;

import com.web.billim.security.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request,AUTHORIZATION_HEADER);
        try {
            if(jwt != null && jwtTokenProvider.tokenValidation(jwt)){ // 토큰 값이 없지 않고, 토큰유효성 검사 메서드를 통과한 경우
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("set Authentication to security context for '{}', uri: {}", authentication.getName(), request.getRequestURI());
            }
        } catch(ExpiredJwtException e){
            request.setAttribute("exception", e);
            log.info("ExpiredJwtException : {}", e.getMessage());
        } catch(JwtException | IllegalArgumentException e){
            request.setAttribute("exception", e);
            log.info("jwtException : {}", e.getMessage());
        }
        // filterChain 에서 다음 filter 가 호출되게 하거나 마지막 필터일경우 체인 끝에 있는 리소스가 호출되게 해주는 메소드
        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request, String header){
        String bearerToken = request.getHeader(header);
        if(bearerToken != null && bearerToken.startsWith("Bearer-")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
