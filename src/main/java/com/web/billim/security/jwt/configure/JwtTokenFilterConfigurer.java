package com.web.billim.security.jwt.configure;

import com.web.billim.redis.service.JwtTokenRedisService;
import com.web.billim.security.filter.JwtAuthenticationFilter;
import com.web.billim.security.filter.LoginAuthenticationFilter;
import com.web.billim.security.jwt.provider.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRedisService jwtTokenRedisService;


    public JwtTokenFilterConfigurer(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, JwtTokenRedisService jwtTokenRedisService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.jwtTokenRedisService = jwtTokenRedisService;

    }

    @Override
    public void configure(HttpSecurity builder){

        // JWT token 인증 provider
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);

        // Login filter
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(authenticationManager,jwtTokenProvider,jwtTokenRedisService);

        builder.addFilterBefore(loginAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
        builder.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
