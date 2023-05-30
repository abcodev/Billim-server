package com.web.billim.security.config;

import com.web.billim.redis.service.JwtTokenRedisService;;
import com.web.billim.security.provider.JwtAuthenticationProvider;
import com.web.billim.security.provider.UsernamPasswordAuthenticationProvider;
import com.web.billim.security.jwt.configure.JwtTokenFilterConfigurer;
import com.web.billim.security.jwt.provider.JwtTokenProvider;

import com.web.billim.security.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailServiceImpl userDetailsService;

    private final JwtTokenRedisService jwtTokenRedisService;


    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager,HttpSecurity http) throws Exception{
        http.csrf().disable()
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
                .apply(new JwtTokenFilterConfigurer(jwtTokenProvider, authenticationManager,jwtTokenRedisService));

        return http.build();
    }
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> {
//            web.ignoring()
//                    .antMatchers(
//                            "/","product/list","product/detail/**","product/category",
//                            "/v3/api-docs", "/configuration/ui", "/swagger-resources/**",
//                            "/configuration/security", "/swagger-ui.html/**", "/swagger-ui/**", "/webjars/**", "/swagger/**"
//                    );
//        };
//    }


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
        return new JwtAuthenticationProvider(jwtTokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
