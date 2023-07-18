package com.web.billim.security.config;

import com.web.billim.jwt.filter.JwtAuthenticationFilter;
import com.web.billim.jwt.filter.JwtExceptionFilter;
import com.web.billim.jwt.JwtProvider;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.security.LoginAuthenticationFilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;


    public SecurityFilterConfigurer(JwtProvider jwtProvider, AuthenticationManager authenticationManager, JwtService jwtService, SecurityFilterSkipMatcher securityFilterSkipMatcher) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.securityFilterSkipMatcher = securityFilterSkipMatcher;
    }

    @Override
    public void configure(HttpSecurity builder) {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(authenticationManager, jwtProvider, jwtService);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider,securityFilterSkipMatcher);
        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter(securityFilterSkipMatcher);

        builder.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(jwtExceptionFilter,JwtAuthenticationFilter.class);
        builder.addFilterBefore(loginAuthenticationFilter,JwtExceptionFilter.class);
    }
}
