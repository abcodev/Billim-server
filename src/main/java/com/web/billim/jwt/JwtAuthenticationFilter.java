package com.web.billim.jwt;

import com.web.billim.common.exception.JwtException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.jwt.dto.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String jwt = resolveToken((HttpServletRequest) request, AUTHORIZATION_HEADER);
            if (jwtUtils.tokenValidation(jwt)) {
                JwtAuthenticationToken jwtAuthenticationToken = jwtUtils.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            }
        } catch (AuthenticationException e){
            throw  new JwtException(ErrorCode.EXPIRED_TOKEN);
        }
            chain.doFilter(request, response);
    }
    private String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
////        try {
//            String jwt = resolveToken(request, AUTHORIZATION_HEADER);
//            if (jwtUtils.tokenValidation(jwt)) {
//                JwtAuthenticationToken jwtAuthenticationToken = jwtUtils.getAuthentication(jwt);
//                SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
//            }
////        } catch (Exception e) {
////            request.setAttribute("exception","테스트중");
////            throw new JwtException(ErrorCode.EXPIRED_TOKEN);
////        }
//            filterChain.doFilter(request, response);
//    }
