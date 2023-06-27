package com.web.billim.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.jwt.dto.JwtAuthenticationToken;
import com.web.billim.security.SecurityFilterSkipMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final AuthenticationManager authenticationManager;
    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityFilterSkipMatcher securityFilterSkipMatcher){
        this.authenticationManager = authenticationManager;
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
            try {
                JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authenticationManager.authenticate(new JwtAuthenticationToken(jwt));
                if(jwtAuthenticationToken.isAuthenticated()) {
                    if (!(request.getRequestURI().equals("/auth/reIssue/token"))) {
                        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                    }
                }
                filterChain.doFilter(request,response);
            }catch (AuthenticationException authenticationException){
                SecurityContextHolder.clearContext();

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try (OutputStream outputStream = response.getOutputStream()) {
                    new ObjectMapper().writeValue(outputStream, authenticationException.getMessage());
                    outputStream.flush();
                }
            }
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return securityFilterSkipMatcher.shouldSkipFilter(request);
    }
}
