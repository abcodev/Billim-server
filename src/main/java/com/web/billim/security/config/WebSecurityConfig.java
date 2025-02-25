package com.web.billim.security.config;

import com.web.billim.jwt.filter.JwtAuthenticationFilter;
import com.web.billim.jwt.filter.JwtExceptionFilter;
import com.web.billim.jwt.provider.JwtProvider;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.oauth.CustomOAuthTokenResponseConverter;
import com.web.billim.security.filter.LoginAuthenticationFilter;
import com.web.billim.security.provider.UsernamPasswordAuthenticationProvider;

import com.web.billim.security.service.UserDetailServiceImpl;

import com.web.billim.oauth.OAuth2LoginSuccessHandler;
import com.web.billim.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;
    private final OAuthService oauthService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager, HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeHttpRequests() // 변경된 부분
            .requestMatchers("/**").permitAll() // permitAll 변경
            .anyRequest().authenticated()

            .and()
            .apply(jwtTokenFilterConfigurer(jwtProvider, authenticationManager, jwtService, securityFilterSkipMatcher))

            .and()
            .oauth2Login()
            .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
            .and()
            .redirectionEndpoint().baseUri("/oauth/kakao")
            .and()
            .userInfoEndpoint().userService(oauthService)
            .and()
            .successHandler(oAuth2LoginSuccessHandler);

        return http.build();
    }


//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://yourdomain.com"));  // 허용할 도메인 추가
//        corsConfig.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
//        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        corsConfig.setAllowCredentials(true);  // 자격 증명 허용 (쿠키 포함 등)
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);  // 모든 요청에 대해 CORS 설정 적용
//
//        return source;
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        return new CorsFilter(corsConfigurationSource());
//    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient authorizationCodeTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();

        OAuth2AccessTokenResponseHttpMessageConverter converter = new OAuth2AccessTokenResponseHttpMessageConverter();
        converter.setAccessTokenResponseConverter(new CustomOAuthTokenResponseConverter());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), converter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        authorizationCodeTokenResponseClient.setRestOperations(restTemplate);
        return authorizationCodeTokenResponseClient;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception { // 변경된 부분
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(usernamPasswordAuthenticationProvider());
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
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider, securityFilterSkipMatcher, jwtService);
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter(
        AuthenticationManager authenticationManager,  // 변경된 부분
        JwtProvider jwtProvider,
        JwtService jwtService
    ) {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(authenticationManager, jwtProvider, jwtService);
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return loginAuthenticationFilter;
    }

    @Bean
    public UsernamPasswordAuthenticationProvider usernamPasswordAuthenticationProvider() {
        return new UsernamPasswordAuthenticationProvider(userDetailsService, passwordEncoder);
    }

}
