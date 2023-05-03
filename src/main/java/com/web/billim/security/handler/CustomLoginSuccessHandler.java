//package com.web.billim.security.handler;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        // 나중에 사용자의 정보를 꺼낼 경우에도 SecurityContextHolder의 context에서 조회 가능함
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        response.sendRedirect("/");
//    }
//}