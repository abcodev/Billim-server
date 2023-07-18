package com.web.billim.member.service;

import com.web.billim.common.exception.JwtException;
import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.jwt.JwtProvider;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.web.billim.common.exception.handler.ErrorCode.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    private final MemberService memberService;


    public void logout(long memberId) {
        String token = " ";
        Authentication authentication = jwtProvider.getAuthentication(token);

        if(jwtService.existsById(memberId)){
            jwtService.deleteRefreshToken(memberId);
        }
    }

    @Transactional
    public ReIssueTokenResponse reIssueToken(ReIssueTokenRequest request) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();

        log.info("refreshToken 유효성 검사");
        jwtProvider.tokenValidation(refreshToken);

        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        Member member = memberService.findById(Long.parseLong(authentication.getPrincipal().toString()));

        log.info("회원번호 검사");
        jwtService.compareToken(refreshToken,member.getMemberId());

        log.info("기존 token 삭제");
        jwtService.deleteRefreshToken(member.getMemberId());

        String newAccessToken = jwtProvider.createAccessToken(String.valueOf(member.getMemberId()),member.getGrade());
        String newRefreshToken = jwtProvider.createRefreshToken(String.valueOf(member.getMemberId()));

        log.info("새로운 토큰 redis 저장");
        jwtService.saveToken(new RedisJwt(member.getMemberId(),newRefreshToken));
        return new ReIssueTokenResponse(member.getMemberId(),newAccessToken,newRefreshToken);
    }
}
