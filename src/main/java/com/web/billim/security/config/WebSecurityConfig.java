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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final SecurityFilterSkipMatcher securityFilterSkipMatcher;
    private final OAuthService oauthService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtExceptionFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter(jwtProvider), JwtExceptionFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .tokenEndpoint(token -> token.accessTokenResponseClient(accessTokenResponseClient()))
                .redirectionEndpoint(redirection -> redirection.baseUri("/oauth/kakao"))
                .userInfoEndpoint(userInfo -> userInfo.userService(oauthService))
                .successHandler(oAuth2LoginSuccessHandler)
            )
            .build();
    }

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
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(usernamPasswordAuthenticationProvider()));
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
        AuthenticationManager authenticationManager,
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
