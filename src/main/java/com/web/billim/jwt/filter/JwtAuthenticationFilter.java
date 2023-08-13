package com.web.billim.jwt.filter;

import com.web.billim.exception.JwtException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.jwt.JwtProvider;
import com.web.billim.jwt.dto.JwtAuthenticationToken;
import com.web.billim.jwt.service.JwtService;
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
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, SecurityFilterSkipMatcher securityFilterSkipMatcher, JwtService jwtService) {
        this.jwtProvider = jwtProvider;
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("요청1 : " + request.getRequestURL());
        String jwt = resolveToken(request);
        if (jwtProvider.tokenValidation(jwt)) {
            if(jwtService.checkBlackList(jwt)){
                log.error("해당 토큰은 blackList 에 등록된 토큰 입니다.");
                throw new JwtException(ErrorCode.INVALID_TOKEN);
            }
            JwtAuthenticationToken jwtAuthenticationToken = jwtProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            filterChain.doFilter(request, response);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
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