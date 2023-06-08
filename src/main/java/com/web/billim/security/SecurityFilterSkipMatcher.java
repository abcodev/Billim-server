package com.web.billim.security;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
@Component
public class SecurityFilterSkipMatcher{

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final String[] excludedPaths = {
            "/**",
            //"/product/list", "/product/detail/**", "/product/category", "/member/signup",
            //            "/member/send/email",

            
            "/v3/api-docs", "/configuration/ui", "/swagger-resources/**",
            "/configuration/security", "/swagger-ui.html/**", "/swagger-ui/**", "/webjars/**", "/swagger/**",
            "/auth/reIssue/token"
    };
    public boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(excludedPaths)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
