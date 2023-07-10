package com.web.billim.jwt;

import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.security.SecurityFilterSkipMatcher;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtExceptionFilter extends OncePerRequestFilter {

    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;

    public JwtExceptionFilter(SecurityFilterSkipMatcher securityFilterSkipMatcher) {
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request,response);
        } catch (ExpiredJwtException e){
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN);
            return;
        } catch (MalformedJwtException e){
            request.setAttribute("exception",ErrorCode.WRONG_TYPE_TOKEN);
            return;
        } catch (SignatureException e){
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
