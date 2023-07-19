package com.web.billim.jwt.filter;

import com.web.billim.jwt.JwtProvider;
import com.web.billim.jwt.JwtAuthenticationToken;
import com.web.billim.security.config.SecurityFilterSkipMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtProvider jwtProvider;

    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, SecurityFilterSkipMatcher securityFilterSkipMatcher) {
        this.jwtProvider = jwtProvider;
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request, AUTHORIZATION_HEADER);
        if (jwtProvider.tokenValidation(jwt)) {
            JwtAuthenticationToken jwtAuthenticationToken = jwtProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            filterChain.doFilter(request, response);
        }
    }

    private String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return securityFilterSkipMatcher.shouldSkipFilter(request);
    }
}