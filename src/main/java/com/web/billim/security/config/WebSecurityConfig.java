package com.web.billim.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.security.CustomAuthenticationProvider;
import com.web.billim.security.filter.LoginCheckFilter;
import com.web.billim.security.handler.RestAuthenticationFailureHandler;
import com.web.billim.security.handler.RestauthenticationSuccessHandler;
import com.web.billim.security.jwt.configure.JwtTokenFilterConfigurer;
import com.web.billim.security.jwt.provider.JwtTokenProvider;
import com.web.billim.security.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public LoginCheckFilter loginCheckFilter(AuthenticationManager authenticationManager) {
        LoginCheckFilter filter = new LoginCheckFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return filter;

    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
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
                .apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new RestauthenticationSuccessHandler(jwtTokenProvider);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(UserDetailServiceImpl userService) {
        return new CustomAuthenticationProvider(userService, passwordEncoder(),jwtTokenProvider);
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
