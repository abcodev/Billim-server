package com.web.billim.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.billim.jwt.JwtProvider;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.oauth.dto.OauthMember;
import com.web.billim.security.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("카카오 로그인 성공");
        OauthMember oauthMember  = (OauthMember) authentication.getPrincipal();
        long memberId = oauthMember.getMemberId();
        MemberGrade memberGrade = MemberGrade.valueOf(oauthMember.getGrade().name());

        String accessToken = jwtProvider.createAccessToken(String.valueOf(memberId),memberGrade);
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(memberId));

        RedisJwt redisJwt = new RedisJwt(memberId,refreshToken);
        jwtService.saveToken(redisJwt);

        LoginResponse loginResponse = new LoginResponse(memberId,accessToken,refreshToken);
        String body = new ObjectMapper().writeValueAsString(loginResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
    }
}
