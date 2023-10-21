package com.web.billim.member.service;

import com.web.billim.exception.JwtException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.jwt.provider.JwtProvider;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    private final MemberDomainService memberDomainService;

    @Transactional
    public void logout(String accessToken) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        // refresh Token 유효한지
        if (jwtService.existsById(memberId)) {
            String refreshToken = jwtService.getRefreshToken(memberId);

            jwtProvider.tokenValidation(refreshToken);

            log.info("refreshToken 존재 여부 / 있으면 삭제시키기");
            jwtService.deleteRefreshToken(memberId);

            log.info("기존 accessToken BlackList 로 저장");
            Date expiration = jwtProvider.getExpriedAt(accessToken);
            jwtService.blackList(memberId, accessToken, expiration);
        }
    }

    @Transactional
    public ReIssueTokenResponse reIssueToken(ReIssueTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        log.info("refreshToken 유효성 검사");
        try {
            jwtProvider.tokenValidation(refreshToken);
        } catch (JwtException ex) {
            if (ex.getErrorCode().equals(ErrorCode.EXPIRED_TOKEN)) {
                throw new JwtException(ErrorCode.EXPIRED_REFRESH_TOKEN);
            } else {
                throw new JwtException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
        }

        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        Member member = memberDomainService.retrieve(Long.parseLong(authentication.getPrincipal().toString()));

        log.info("회원번호 검사");
        jwtService.compareToken(refreshToken, member.getMemberId());

        log.info("기존 token 삭제");
        jwtService.deleteRefreshToken(String.valueOf(member.getMemberId()));

        String newAccessToken = jwtProvider.createAccessToken(String.valueOf(member.getMemberId()), member.getGrade());
        String newRefreshToken = jwtProvider.createRefreshToken(String.valueOf(member.getMemberId()));

        log.info("새로운 토큰 redis 저장");
        jwtService.saveToken(new RedisJwt(member.getMemberId(), newRefreshToken));
        return new ReIssueTokenResponse(member.getMemberId(), newAccessToken, newRefreshToken);
    }

}
