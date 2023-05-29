package com.web.billim.security.provider;

import com.web.billim.security.jwt.domain.BillimAuthentication;
import com.web.billim.security.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwt = authentication.getCredentials().toString();
        if (jwt != null && jwtTokenProvider.tokenValidation(jwt)) { // 토큰 값이 없지 않고, 토큰유효성 검사 메서드를 통과한 경우
            Authentication auth = jwtTokenProvider.getAuthentication(jwt);
            return auth;
        }
        throw  new BadCredentialsException("Invalid JWT token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BillimAuthentication.class.isAssignableFrom(authentication);
    }
}
