package com.web.billim.security.config;

import com.web.billim.jwt.*;
import com.web.billim.jwt.filter.JwtAuthenticationFilter;
import com.web.billim.jwt.filter.JwtExceptionFilter;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.security.LoginAuthenticationFilter;
import com.web.billim.security.UsernamPasswordAuthenticationProvider;

import com.web.billim.security.UserDetailServiceImpl;

import com.web.billim.oauth.OAuth2LoginSuccessHandler;
import com.web.billim.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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

    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;
    private final OauthService oauthService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager,HttpSecurity http) throws Exception {

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
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(jwtTokenFilterConfigurer(jwtProvider,authenticationManager, jwtService,securityFilterSkipMatcher))

                .and()
                .oauth2Login()
                .redirectionEndpoint().baseUri("/oauth/kakao")
                .and().userInfoEndpoint().userService(oauthService)
                .and().successHandler(oAuth2LoginSuccessHandler);

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
//        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterConfigurer jwtTokenFilterConfigurer(
            JwtProvider jwtProvider,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            SecurityFilterSkipMatcher securityFilterSkipMatcher
    ) {
        return new SecurityFilterConfigurer(jwtProvider, authenticationManager, jwtService, securityFilterSkipMatcher);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(securityFilterSkipMatcher);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider){
        return new JwtAuthenticationFilter(jwtProvider,securityFilterSkipMatcher, jwtService);
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter(
            AuthenticationManager configureAuthenticationManager,
            JwtProvider jwtProvider,
            JwtService jwtService
    ) {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(configureAuthenticationManager, jwtProvider, jwtService);
        loginAuthenticationFilter.setAuthenticationManager(configureAuthenticationManager);
        return loginAuthenticationFilter;
    }

    @Bean
    public UsernamPasswordAuthenticationProvider usernamPasswordAuthenticationProvider() {
        return new UsernamPasswordAuthenticationProvider(userDetailsService,passwordEncoder);
    }
}
