//package com.web.billim.security.provider;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@RequiredArgsConstructor
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private final UserDetailsService userDetailsService;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @Override // 실제 인증에 대한 부분
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;
//
//        // AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회
//        String userId = token.getName();
//        String password = (String)token.getCredentials();
////        User user = (User)userDetailsService.loadUserByUsername(userId);
//        UserDetails user = userDetailsService.loadUserByUsername(userId);
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
////            throw new BadCredentialsException(user.getMember().getUserId() + " Invalid password");
//            throw new BadCredentialsException(user.getUsername() + " Invalid password");
//        }
//        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}