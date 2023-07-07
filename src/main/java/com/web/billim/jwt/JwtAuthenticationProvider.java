package com.web.billim.jwt;

import com.web.billim.common.exception.TokenExpiredException;
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
        String jwt = (String) authentication.getCredentials();
        if (jwt != null && jwtUtils.tokenValidation(jwt)) {
            return jwtUtils.getAuthentication(jwt);
        }
        return null;

//        try {
//            if (jwt != null && jwtUtils.tokenValidation(jwt)) {
//                return jwtUtils.getAuthentication(jwt);
//            } else {
//                throw new BadCredentialsException("Invalid JWT token");
//            }
//        } catch (TokenExpiredException e) {
//            throw new RuntimeException("토큰이 만료되었습니다.");
//        } catch (Exception e) {
//            throw new BadCredentialsException("Invalid JWT token");
//        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
