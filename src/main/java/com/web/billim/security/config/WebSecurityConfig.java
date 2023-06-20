package com.web.billim.security.config;

import com.web.billim.jwt.JwtTokenRedisService;
import com.web.billim.jwt.JwtAuthenticationProvider;
import com.web.billim.security.SecurityFilterSkipMatcher;
import com.web.billim.security.UsernamPasswordAuthenticationProvider;
import com.web.billim.jwt.JwtTokenFilterConfigurer;
import com.web.billim.jwt.JwtUtils;

import com.web.billim.security.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtils jwtUtils;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtTokenRedisService jwtTokenRedisService;
    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager,HttpSecurity http) throws Exception{

        http
                .cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new JwtTokenFilterConfigurer(jwtUtils, authenticationManager,jwtTokenRedisService, securityFilterSkipMatcher));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager configureAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(usernamPasswordAuthenticationProvider());
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public UsernamPasswordAuthenticationProvider usernamPasswordAuthenticationProvider(){
        return new UsernamPasswordAuthenticationProvider(userDetailsService,passwordEncoder());
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider(jwtUtils);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
