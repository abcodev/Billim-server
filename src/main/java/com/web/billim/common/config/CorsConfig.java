package com.web.billim.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://billim.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("http://localhost:3000");
//        config.addAllowedOrigin("^https?:\\/\\/billim.vercel.app");
//        config.addAllowedHeader(CorsConfiguration.ALL);
//        config.addAllowedMethod(HttpMethod.GET);
//        config.addAllowedMethod(HttpMethod.POST);
//        config.addAllowedMethod(HttpMethod.HEAD);
//        config.addAllowedMethod(HttpMethod.PUT);
//        config.addAllowedMethod(HttpMethod.DELETE);
//        config.addAllowedMethod(HttpMethod.TRACE);
//        config.addAllowedMethod(HttpMethod.OPTIONS);
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//
//        source.registerCorsConfiguration("^https?:\\/\\/billim.vercel.app$", config);
//        return new CorsFilter(source);
//    }
//
//}



//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 특정 도메인에서의 요청을 허용
//        config.addAllowedOrigin("http://localhost:3000");
//        config.addAllowedOrigin("https://billim.vercel.app");
//
//        // 허용할 HTTP 메서드
//        config.addAllowedMethod(HttpMethod.GET);
//        config.addAllowedMethod(HttpMethod.POST);
//        config.addAllowedMethod(HttpMethod.HEAD);
//        config.addAllowedMethod(HttpMethod.PUT);
//        config.addAllowedMethod(HttpMethod.DELETE);
//        config.addAllowedMethod(HttpMethod.TRACE);
//        config.addAllowedMethod(HttpMethod.OPTIONS);
//
//        // 허용할 헤더
//        config.addAllowedHeader(CorsConfiguration.ALL);
//
//        // Credentials 허용 (즉, 인증된 요청 허용)
//        config.setAllowCredentials(true);
//
//        // preflight 요청의 결과를 캐시 (1시간 동안 캐시)
//        config.setMaxAge(3600L);
//
//        // 특정 URL 패턴에 대한 CORS 설정
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }
//}



