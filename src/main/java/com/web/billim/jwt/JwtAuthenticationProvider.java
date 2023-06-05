package com.web.billim.jwt;

import com.web.billim.jwt.dto.JwtAuthenticationToken;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationProvider(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwt = authentication.getCredentials().toString();
        if (jwt != null && jwtUtils.tokenValidation(jwt)) { // 토큰 값이 없지 않고, 토큰유효성 검사 메서드를 통과한 경우
            return jwtUtils.getAuthentication(jwt);
        }
        throw  new BadCredentialsException("Invalid JWT token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
