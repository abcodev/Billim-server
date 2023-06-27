package com.web.billim.jwt;

import com.web.billim.security.LoginAuthenticationFilter;
import com.web.billim.security.SecurityFilterSkipMatcher;
import com.web.billim.security.handler.AuthenticationFailureEntryPoint;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRedisService jwtTokenRedisService;
    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;
    private final AuthenticationFailureEntryPoint authenticationFailureEntryPoint;


    public JwtTokenFilterConfigurer(JwtUtils jwtUtils, AuthenticationManager authenticationManager, JwtTokenRedisService jwtTokenRedisService, SecurityFilterSkipMatcher securityFilterSkipMatcher, AuthenticationFailureEntryPoint authenticationFailureEntryPoint) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.jwtTokenRedisService = jwtTokenRedisService;
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
        this.authenticationFailureEntryPoint = authenticationFailureEntryPoint;
    }

    @Override
    public void configure(HttpSecurity builder){

        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(authenticationManager, jwtUtils, jwtTokenRedisService);
        loginAuthenticationFilter.setAuthenticationFailureHandler(new AuthenticationEntryPointFailureHandler(authenticationFailureEntryPoint));
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, securityFilterSkipMatcher);

        builder.addFilterBefore(loginAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
