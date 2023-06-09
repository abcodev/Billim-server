package com.web.billim.jwt;

import com.web.billim.jwt.dto.JwtAuthenticationToken;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        try {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authenticationManager.authenticate(new JwtAuthenticationToken(jwt));
            if (jwtAuthenticationToken.isAuthenticated()) {
                if (!(request.getRequestURI().equals("/auth/reIssue/token"))) {
                    // 클리어 하고 (이전게 남아있을 수 있어서)
                    // SecurityContextHolder.clearContext();
                    // SecurityContext context = SecurityContextHolder.createEmptyContext();
                    // context.setAuthentication(jwtAuthenticationToken);
                    // SecurityContextHolder.setContext(context);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                }
            }
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Bearer- memberId 가 null 로 넘어올 때 illegalArgumentException , 토큰이 넘어오는데 파싱을 못함
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "product/list", "product/detail/**", "product/category", "/member/signup",
                "/v3/api-docs", "/configuration/ui", "/swagger-resources/**",
                "/configuration/security", "/swagger-ui.html/**", "/swagger-ui/**", "/webjars/**", "/swagger/**"
                , "/auth/reIssue/token", "/member/send/email"
        };
        String path = request.getRequestURI();
        // "/" 문자열로 비교해서 "/".startsWith(path) => true 가 나옴
        // TODO : RequestMatcher 방식으로 고쳐야할것같다
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
