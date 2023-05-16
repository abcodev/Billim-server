package com.web.billim.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.security.CustomAuthenticationProvider;
import com.web.billim.security.filter.UsernamePasswordAuthenticationFilter;
import com.web.billim.security.handler.RestAuthenticationFailureHandler;
import com.web.billim.security.handler.RestauthenticationSuccessHandler;
import com.web.billim.security.service.UserDetailServiceImpl;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception{
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(authenticationManagerBean(),objectMapper());
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

//                 security 은 기본적으로 session 기반이지만 우리는 token을 사용하기 때문에 세션을 사용하지 않는다고 설정
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()

                .antMatchers("/**").permitAll()
//                .antMatchers("/member/signup").permitAll()
                .anyRequest().authenticated();
    }



    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new RestauthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(UserDetailServiceImpl userService) {
        return new CustomAuthenticationProvider(userService, passwordEncoder());
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
