package com.web.billim.jwt;

import com.web.billim.jwt.dto.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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



    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authenticationManager.authenticate(new JwtAuthenticationToken(jwt));
        if(jwtAuthenticationToken.isAuthenticated()){
            if(!(request.getRequestURI().equals("/auth/reIssue/token"))) {
                SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
        if(bearerToken != null && bearerToken.startsWith("Bearer-")){
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String[] excludePath = {
                "/","product/list","product/detail/**","product/category","/member/signup",
                "/v3/api-docs", "/configuration/ui", "/swagger-resources/**",
                "/configuration/security", "/swagger-ui.html/**", "/swagger-ui/**", "/webjars/**", "/swagger/**"
                ,"/auth/reIssue/token"
        };
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
