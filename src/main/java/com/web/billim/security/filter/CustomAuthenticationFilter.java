//package com.web.billim.security.filter;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    // UsernamePasswordAuthenticationFilter
//    // spring security 에서 폼 형식의 로그인 형태에서 사용하는 filter
//    // SecurityConfig에 http.formLogin() 작성하면 자동으로 사용되어짐
//    // 기본 값으로 username password 를 사용하지만 userId, password 를 사용하여 가독성을 높이기 위해 커스텀을 진행
//
//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super.setAuthenticationManager(authenticationManager);
//    }
//
//    @Override   //원래는 username/password가 기본값이지만 우리 프로젝트에서는 userid/password로 로그인하기 위해 오버라이딩
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        if(request.getParameter("userId")!=null && request.getParameter("password")!=null){
//            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getParameter("userid"), request.getParameter("password"));
//            setDetails(request, authRequest);
//            return this.getAuthenticationManager().authenticate(authRequest);
//        }
//        return null;
//    }
//}
