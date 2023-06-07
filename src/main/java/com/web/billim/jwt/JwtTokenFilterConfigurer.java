package com.web.billim.jwt;

import com.web.billim.security.LoginAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRedisService jwtTokenRedisService;


    public JwtTokenFilterConfigurer(JwtUtils jwtUtils, AuthenticationManager authenticationManager, JwtTokenRedisService jwtTokenRedisService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.jwtTokenRedisService = jwtTokenRedisService;

    }

    @Override
    public void configure(HttpSecurity builder){

        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(authenticationManager, jwtUtils,jwtTokenRedisService);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);

        builder.addFilterBefore(loginAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
