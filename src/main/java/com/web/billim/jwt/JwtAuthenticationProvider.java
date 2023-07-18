//package com.web.billim.jwt;
//
//import com.web.billim.common.exception.UnAuthorizedException;
//import com.web.billim.common.exception.handler.ErrorCode;
//import com.web.billim.jwt.dto.JwtAuthenticationToken;
//
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//
//public class JwtAuthenticationProvider implements AuthenticationProvider {
//
//    private final JwtUtils jwtUtils;
//
//    public JwtAuthenticationProvider(JwtUtils jwtUtils) {
//        this.jwtUtils = jwtUtils;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        String jwt = (String) authentication.getCredentials();
//        if(jwtUtils.tokenValidation(jwt)){
//            return jwtUtils.getAuthentication(jwt);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
